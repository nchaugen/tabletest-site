#!/usr/bin/env python3
"""Build static/images/og-card.png, the social card that Slack, LinkedIn and X show
when someone links to tabletest.org.

The card is the leap year screenshot on a white 1200x630 canvas. The script trims the
uneven margins of the screenshot and centres what is left, so the code block sits in
the middle of the card.

Run from the repository root, no dependencies:

    python3 scripts/og-card.py
"""

import struct
import sys
import zlib
from pathlib import Path

SOURCE = Path("content/leap-year-table-old.png")
TARGET = Path("static/images/og-card.png")
CARD_WIDTH = 1200
CARD_HEIGHT = 630
DPI_144 = 5669  # pixels per metre, the density of the source screenshot
WHITE = (255, 255, 255)

CHANNELS_PER_COLOUR_TYPE = {0: 1, 2: 3, 3: 1, 4: 2, 6: 4}


def read_png(path):
    """Return the width, the bytes per pixel and the unfiltered rows of a PNG file."""
    data = path.read_bytes()
    position = 8
    compressed = b""
    while position < len(data):
        length = struct.unpack(">I", data[position:position + 4])[0]
        kind = data[position + 4:position + 8]
        chunk = data[position + 8:position + 8 + length]
        if kind == b"IHDR":
            width, height, depth, colour_type = struct.unpack(">IIBB", chunk[:10])
        elif kind == b"IDAT":
            compressed += chunk
        position += 12 + length
    if depth != 8:
        sys.exit(f"{path}: only 8 bits per channel is supported, found {depth}")
    pixel_bytes = CHANNELS_PER_COLOUR_TYPE[colour_type]
    return width, pixel_bytes, unfilter(zlib.decompress(compressed), width, height, pixel_bytes)


def unfilter(raw, width, height, pixel_bytes):
    """Undo the per-row filter that PNG applies before compression."""
    stride = width * pixel_bytes
    rows = []
    previous = bytearray(stride)
    position = 0
    for _ in range(height):
        filter_type = raw[position]
        position += 1
        row = bytearray(raw[position:position + stride])
        position += stride
        for index in range(stride):
            left = row[index - pixel_bytes] if index >= pixel_bytes else 0
            above = previous[index]
            above_left = previous[index - pixel_bytes] if index >= pixel_bytes else 0
            if filter_type == 1:
                row[index] = (row[index] + left) & 255
            elif filter_type == 2:
                row[index] = (row[index] + above) & 255
            elif filter_type == 3:
                row[index] = (row[index] + ((left + above) >> 1)) & 255
            elif filter_type == 4:
                row[index] = (row[index] + paeth(left, above, above_left)) & 255
        rows.append(row)
        previous = row
    return rows


def paeth(left, above, above_left):
    estimate = left + above - above_left
    distances = (abs(estimate - left), abs(estimate - above), abs(estimate - above_left))
    if distances[0] <= distances[1] and distances[0] <= distances[2]:
        return left
    return above if distances[1] <= distances[2] else above_left


def colour_at(row, x, pixel_bytes):
    pixel = row[x * pixel_bytes:x * pixel_bytes + pixel_bytes]
    if pixel_bytes >= 3:
        return pixel[0], pixel[1], pixel[2]
    return pixel[0], pixel[0], pixel[0]


def content_bounds(width, pixel_bytes, rows):
    """Return the box that holds every pixel the eye can tell from the white margin."""
    left, top, right, bottom = width, len(rows), -1, -1
    for y, row in enumerate(rows):
        for x in range(width):
            if min(colour_at(row, x, pixel_bytes)) <= 250:
                left = min(left, x)
                right = max(right, x)
                top = min(top, y)
                bottom = max(bottom, y)
    return left, top, right - left + 1, bottom - top + 1


def centre_on_card(width, pixel_bytes, rows, bounds):
    left, top, content_width, content_height = bounds
    if content_width > CARD_WIDTH or content_height > CARD_HEIGHT:
        sys.exit(f"content {content_width}x{content_height} does not fit the card")
    card = [bytearray(bytes(WHITE) * CARD_WIDTH) for _ in range(CARD_HEIGHT)]
    offset_x = (CARD_WIDTH - content_width) // 2
    offset_y = (CARD_HEIGHT - content_height) // 2
    for y in range(content_height):
        row = rows[top + y]
        target = card[offset_y + y]
        for x in range(content_width):
            start = (offset_x + x) * 3
            target[start:start + 3] = bytes(colour_at(row, left + x, pixel_bytes))
    return card


def write_png(path, rows):
    def chunk(kind, payload):
        crc = zlib.crc32(kind + payload) & 0xFFFFFFFF
        return struct.pack(">I", len(payload)) + kind + payload + struct.pack(">I", crc)

    header = struct.pack(">IIBBBBB", CARD_WIDTH, CARD_HEIGHT, 8, 2, 0, 0, 0)
    density = struct.pack(">IIB", DPI_144, DPI_144, 1)
    pixels = zlib.compress(b"".join(b"\x00" + bytes(row) for row in rows), 9)
    path.write_bytes(
        b"\x89PNG\r\n\x1a\n"
        + chunk(b"IHDR", header)
        + chunk(b"pHYs", density)
        + chunk(b"IDAT", pixels)
        + chunk(b"IEND", b"")
    )


def main():
    if not SOURCE.exists():
        sys.exit(f"{SOURCE} not found — run the script from the repository root")
    width, pixel_bytes, rows = read_png(SOURCE)
    bounds = content_bounds(width, pixel_bytes, rows)
    write_png(TARGET, centre_on_card(width, pixel_bytes, rows, bounds))
    print(f"{TARGET}: {CARD_WIDTH}x{CARD_HEIGHT} from {SOURCE} content {bounds[2]}x{bounds[3]}")


if __name__ == "__main__":
    main()

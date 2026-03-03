# TableTest Website

Marketing and documentation site for [TableTest](https://github.com/nchaugen/tabletest) — a JUnit extension for table-driven testing on the JVM.

Built with [Hugo](https://gohugo.io/) and the [Hextra](https://github.com/imfing/hextra) theme:
* [Hugo documentation](https://gohugo.io/documentation/)
* [Hextra documentation](https://imfing.github.io/hextra/docs/)

## Local Development

Prerequisites: [Hugo](https://gohugo.io/getting-started/installing/) and [Go](https://golang.org/doc/install)

```shell
hugo server --logLevel debug --disableFastRender -p 1313
```

### Update Theme

```shell
hugo mod get -u github.com/imfing/hextra && hugo mod tidy
```

## Related Repositories

- [tabletest](https://github.com/nchaugen/tabletest) — Core library
- [tabletest-intellij](https://github.com/nchaugen/tabletest-intellij) — IntelliJ plugin
- [tabletest-vscode](https://github.com/nchaugen/tabletest-vscode) — VS Code extension
- [tabletest-formatter](https://github.com/nchaugen/tabletest-formatter) — Formatting tool
- [tabletest-reporter](https://github.com/nchaugen/tabletest-reporter) — Documentation generator
- [tabletest-claude-plugin](https://github.com/nchaugen/tabletest-claude-plugin) — Claude plugin

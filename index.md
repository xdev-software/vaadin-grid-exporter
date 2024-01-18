[![Published on Vaadin Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0?logo=vaadin)](https://vaadin.com/directory/component/gridexporter-for-vaadin)
[![Latest version](https://img.shields.io/maven-central/v/software.xdev/vaadin-grid-exporter?logo=apache%20maven)](https://mvnrepository.com/artifact/software.xdev/vaadin-grid-exporter)
[![Build](https://img.shields.io/github/actions/workflow/status/xdev-software/vaadin-grid-exporter/checkBuild.yml?branch=develop)](https://github.com/xdev-software/vaadin-grid-exporter/actions/workflows/checkBuild.yml?query=branch%3Adevelop)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=xdev-software_vaadin-grid-exporter&metric=alert_status)](https://sonarcloud.io/dashboard?id=xdev-software_vaadin-grid-exporter)
![Vaadin 24+](https://img.shields.io/badge/Vaadin%20Platform/Flow-24+-00b4f0)

# vaadin-grid-exporter

The Vaadin Grid Exporter can convert any Vaadin Grid to a variety of formats. Out of the box supported formats:

* CSV
* Word (DOCX)
* HTML
* ODS
* ODT
* PDF
* PowerPoint (PPTX)
* RTF
* Plain text
* Excel (XLSX)

It's also easy to extend the Exporter to support your custom format.

Default usage:

```java
GridExporter
	.newWithDefaults(this.grExamples)
	.open();
```

Custom format (see
[JsonGridExporterProvider from Demo](vaadin-grid-exporter-demo/src/main/java/software/xdev/vaadin/gridexport/example/jsonext/JsonGridExporterProvider.java)):

```java
GridExporter
	.newWithDefaults(this.grExamples)
	.loadFromProvider(new JsonGridExporterProvider())
	.open();
```

![demo](assets/preview.gif)

## Installation

[Installation guide for the latest release](https://github.com/xdev-software/vaadin-grid-exporter/releases/latest#Installation)

#### Compatibility with Vaadin

| Vaadin version | GridExporter version |
| --- | --- |
| Vaadin 24+ (latest) | ``3+`` |
| Vaadin 23 | ``2.x`` |


## Run the Demo
* Checkout the repo
* Run ``mvn install && mvn -f vaadin-grid-exporter-demo spring-boot:run``
* Open http://localhost:8080


<details>
  <summary>Show example</summary>
  
  ![demo](assets/demo.avif)
</details>

## Support
If you need support as soon as possible and you can't wait for any pull request, feel free to use [our support](https://xdev.software/en/services/support).

## Contributing
See the [contributing guide](./CONTRIBUTING.md) for detailed instructions on how to get started with our project.

## Dependencies and Licenses
View the [license of the current project](LICENSE) or the [summary including all dependencies](https://xdev-software.github.io/vaadin-grid-exporter/dependencies/)

# 3.2.8
* Updated dependencies

# 3.2.7
* Migrated deployment to _Sonatype Maven Central Portal_ [#155](https://github.com/xdev-software/standard-maven-template/issues/155)
* Updated dependencies

# 3.2.6
* Fix naming so that Vaadin Directory sync works [#318](https://github.com/xdev-software/vaadin-addon-template/issues/318)
* Updated dependencies

# 3.2.5
* Make it possible to better customize ``SpecificConfigComponent``

# 3.2.4
* Updated dependencies

# 3.2.3
* Added dedicated method for adding steps to WizardPanel to make overriding it easier #269
* Updated dependencies

# 3.2.2
* Fix column order being ignored #256

# 3.2.1
* Fix cells truncating data #229
* Updated dependencies

# 3.2.0
* Updated to Vaadin 24.4

# 3.1.0
* Update to [JasperReports 7](https://github.com/xdev-software/dynamicreports-core-for-grid-exporter/blob/develop/CHANGELOG.md#200)
* Updated dependencies

# 3.0.3
* ⚠️ GroupId changed from ``com.xdev-software`` to ``software.xdev``
* Updated dependencies

# 3.0.2
* Fixed not working translations on preview step #153
* Updated dependencies

# 3.0.1
* Fixed compilation problems due to missing ``ecj`` dependency #98
* Updated dependencies

# 3.0.0
⚠️<i>This release contains breaking changes</i>

* Adds support for Vaadin 24+, drops support for Vaadin 23<br/>
  <i>If you are still using Vaadin 23, use the ``3.x`` versions.</i>
  * Requires Java 17+
* Replaced the underlying reporting framework ``dynamicreports`` with [our fork of it](https://github.com/xdev-software/dynamicreports-core-for-grid-exporter) which is specially designed for this project
  * Differences from the original project are roughly described in the [changelog](https://github.com/xdev-software/dynamicreports-core-for-grid-exporter/blob/develop/CHANGELOG.md).
* Removed ``XML`` from the default formats because the exported data was unusable
* Replaced ``VaadinInternalRenderingColumnHeaderResolvingStrategy`` with ``VaadinColumnHeaderResolvingStrategy``
* Updated dependencies

# 2.0.0
* Undocked from RapidClipse
  * Changed package from ``com.rapidclipse.framework.server.reports`` to ``software.xdev.vaadin.grid_exporter``
* Restructured the UI
  * Using a step-by-step-wizard like layout
  * Made formats configurable via UI
* Refactored software architecture to make the component more expandable
* Removed ``XLS`` from default formats due to not included dependency which causes a crash (``XLSX`` still works)
* Updated dependencies

Example usage:

| v1 | v2 |
| --- | --- |
| ``GridExportDialog.open(grid)`` | ``GridExporter.newWithDefaults(grid).open()`` |

# 1.0.3
* Removed unused code

# 1.0.2
* Updated dependencies
  * Vaadin 23.2

# 1.0.1
* Removed unnecessary code and dependencies; Updated jasperreports to latest version

# 1.0.0
<i>Initial release</i>

* Forked project from the RapidClipse Framework (Version 11.0)
* Updated to Vaadin 23
* Removed unnecessary code and dependencies
* Removed requirement that Grid columns need keys
* Reworked localization - can now be explicitly set using ``GridExportLocalizationConfig``

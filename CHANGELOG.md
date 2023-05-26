## 3.0.0
⚠️<i>This release contains breaking changes</i>

* Adds support for Vaadin 24+, drops support for Vaadin 23<br/>
  <i>If you are still using Vaadin 23, use the ``3.x`` versions.</i>
  * Requires Java 17+
* Updated dependencies

## 2.0.0
* Undocked from RapidClipse
  * Changed package from ``com.rapidclipse.framework.server.reports`` to ``software.xdev.vaadin.grid_exporter``
* Restructured the UI
  * Using a step-by-step-wizard like layout
  * Made formats configurable via UI
* Refactored software architecture to make the component more expandable
* Removed XLS from default formats due to not included dependency which causes a crash (XLSX still works)
* Updated dependencies

Example usage:

| v1 | v2 |
| --- | --- |
| ``GridExportDialog.open(grid)`` | ``GridExporter.newWithDefaults(grid).open()`` |

## 1.0.3
* Removed unused code

## 1.0.2
* Updated dependencies
  * Vaadin 23.2

## 1.0.1
* Removed unnecessary code and dependencies; Updated jasperreports to latest version

## 1.0.0
<i>Initial release</i>

* Forked project from the RapidClipse Framework (Version 11.0)
* Updated to Vaadin 23
* Removed unnecessary code and dependencies
* Removed requirement that Grid columns need keys
* Reworked localization - can now be explicitly set using ``GridExportLocalizationConfig``

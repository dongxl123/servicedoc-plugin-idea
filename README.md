ServiceDoc
---

* This is a plugin you can generate serviceDoc from public method.
* Please do aware **This plugin is only for IntelliJ IDEA**.
* This plugin develop for the npm model `servicedoc`, [HomePage](https://www.npmjs.com/package/servicedoc), install command `npm i servicedoc` 

## Install   
- Using IDE built-in plugin system on Windows:
  - <kbd>File</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "ServiceDoc"</kbd> > <kbd>Install Plugin</kbd>
- Using IDE built-in plugin system on MacOs:
  - <kbd>Preferences</kbd> > <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Browse repositories...</kbd> > <kbd>Search for "ServiceDoc"</kbd> > <kbd>Install Plugin</kbd>
- Manually:
  - From official jetbrains store Download the `latest release` and install it manually using <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Install plugin from disk...</kbd>

## Usage
### Use IDE menu

### Use hotkey
Default **Option + Ctrl + Shift + o**(Mac), **Alt + Ctrl + Shift + o** (win)

## Version Info
v1.8
> 
* add pluginIcon_dark.svg

v1.7
> 
* add pluginIcon.svg

v1.6
> 
* add example generate button

v1.5
> 
* Resolve conflict: JavaDocWriter component name collision

v1.4
> 
* Support IDEA earlier version since 162+

v1.3
> 
* bugFix: field support special psiType, eg. BigInteger...
* bugFix: remove Deprecated field 

v1.2
> 
* Optimizing UI Interface 
* bugFix: field description should support special char `space`

v1.1
> 
* Support UI operation
* Only Support for interface class

v1.0
> first version
* Support for public method
* Supports shortcut to open ServiceDoc, default option + ctrl + shift + o (mac), alt + ctrl + shift + o(win)
* Support to generate servicedoc
* Support to modify the doc with memory feature
# Nautilus Plugin NRP

Nautilus plugin for the Next Release Problem (NRP)

[![GitHub Release](https://img.shields.io/github/release/nautilus-framework/nautilus-plugin-nrp.svg)](https://github.com/nautilus-framework/nautilus-plugin-nrp/releases/latest)
[![GitHub contributors](https://img.shields.io/github/contributors/nautilus-framework/nautilus-plugin-nrp.svg)](https://github.com/nautilus-framework/nautilus-plugin-nrp/graphs/contributors)
[![GitHub stars](https://img.shields.io/github/stars/nautilus-framework/nautilus-plugin-nrp.svg)](https://github.com/almende/nautilus-framework/nautilus-plugin-nrp)
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)

## Requirements

You need to download Nautilus Framework in your machine (available at https://github.com/nautilus-framework/nautilus-framework) before installing this plugin

## Usage

Open the pom.xml file at ```nautilus-web``` module (from Nautilus Framework) and add the following maven dependency:

```xml
<dependency>
  <groupId>org.nautilus.plugin</groupId>
  <artifactId>nautilus-plugin-nrp</artifactId>
  <version>1.0.0</version>
</dependency>
```

At ```nautilus-web``` search for `PluginService` and add the following fragment code at ```loadPluginsFromClasspath``` method:

```java
addProblemExtension(new NRPProblemExtension());
```

Run Nautilus Framework as usual. If everything is working well, when you initiate a new execution, the NRP problem is going to be available as follows:

<kbd>
  <img src="https://user-images.githubusercontent.com/114015/94708545-bb346580-0312-11eb-99b6-5fba786e071f.png">
</kbd>

## Objective Functions

This plugin supports the following objective functions

1. Number Of Products
2. Alive Mutants
3. Uncovered Pairs
4. Similarity
5. Cost
6. Unselected Features
7. Unimportant Features

Please feel free to pull request a new set of objectives.

## Problem Instances

This plugins makes available the following feature model (problem instances) for your experiments:

1. **James:** SPL for collaborative web systems [1]
2. **CAS (Car Audio System):** a SPL to manage automotive sound systems [6]
3. **WS (Weather Station):** SPL for weather forecast systems [2]
4. **E-Shop:** an E-commerce SPL [5]
5. **Drupal:** a modular open source web content management framework [4]
6. **Smarthome v2.2:** SPL for a smart residential solution [3]

The following table shows information about each feature model:

| Feature Model | # of Products  | Alive Mutants | Valid Pairs | # of Features |
|------|:---:|:---:|:---:|:---:|
|		James       |       68            |    106     |    75     |        14 |
|		CAS         |       450           |    227     |    183    |        21 |
|		WS          |       504           |    357     |    195    |        22 |
|		E-Shop      |      1152           |     94     |    202    |        22 |
|		Drupal      | ~2.09E9     |    2194    |   1081    |        48 |
|		Smarthome   | ~3.87E9     |    2948    |   1710    |        60 |

## Screenshot

The following image shows an execution considering all objective functions

<kbd>
  <img src="https://user-images.githubusercontent.com/114015/94716124-6695e800-031c-11eb-8d58-2a028b9fd8cb.png">
</kbd>


## Questions or Suggestions

Feel free to create <a href="https://github.com/nautilus-framework/nautilus-plugin-nrp/issues">issues</a> here as you need

## Contribute

Contributions to the this project are very welcome! We can't do this alone! Feel free to fork this project, work on it and then make a pull request.

## Authors

* **Thiago Ferreira** - *Initial work*

See also the list of [contributors](https://github.com/nautilus-framework/nautilus-plugin-nrp/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## References

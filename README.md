# MesLocationsVacances

Notre site de commerce électronique _MesLocationsVacances_ permet la recherche et location de chambres (appart'hôtel) ou gîtes entier ainsi que d'activité pour les vacances en famille ou entre amis.

Ce projet s'inscrit dans le cadre du cours [ECOM](https://air.imag.fr/index.php/ECOM-RICM) à Polytech Grenoble.

> Lien vers la [Fiche du projet](https://air.imag.fr/index.php/ECOM_RICM5_Groupe4_2017)

## Installation / Quick launch locally

You should have **Docker** and **Wildfly** (v10+) installed.

1) **Clone the project**:
```
git clone https://github.com/ECOM2017-MesLocationsVacances/MesLocationsVacances.git
```

2) **Start Wildfly in standalone mode**: 

Go to the wildfly directory and execute `./standalone.sh`

3) **Start a MySQL docker instance** (command below is for Docker but a local installation should work):
```
docker run --name mysqldb -e MYSQL_USER=mysql -e MYSQL_PASSWORD=mysql -e MYSQL_DATABASE=sample -e MYSQL_ROOT_PASSWORD=supersecret -d mysql
```

4) **Build and deploy the app**:
```
cd MesLocationsVacances
mvn wildfly:deploy
```

5) That's it! Just open [localhost:8080/MesLocationsVacances](http://localhost:8080/MesLocationsVacances) in your browser and enjoy :tada:

## API Reference

_Depending on the size of the project, if it is small and simple enough the reference docs can be added to the README. For medium size to larger projects it is important to at least provide a link to where the API reference docs live._

## Tests

_Describe and show how to run the tests with code examples._

## Contributors

* Antoine Boisadam : [Github](https://github.com/Antoine38660)
* Maxime D.
* Lucas L.
* Douria Z.


## License

Copyright 2017 Antoine Boisadam, Maxime D., Lucas L., Douria Z.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

# pocDrools
Poc to test drools in different ways

## Création KieContainer 



## Stateful vs Stateless
- **Stateful**: Les règles sont évaluées en fonction des faits insérés dans la session. Les faits sont conservés dans la session jusqu'à ce que la session soit détruite.
- **Stateless**: Les faits sont insérés dans la session, les règles sont évaluées et les faits sont retirés de la session. La session est détruite après l'évaluation des règles.
- **Stateless KieSession**: Les faits sont insérés dans la session, les règles sont évaluées et les faits sont retirés de la session. La session est détruite après l'évaluation des règles. La session est créée à chaque fois que la méthode newKieSession() est appelée.
- **Stateful KieSession**: Les faits sont insérés dans la session, les règles sont évaluées et les faits sont retirés de la session. La session est détruite après l'évaluation des règles. La session est créée une seule fois et les faits sont insérés dans la session à chaque fois que la méthode insert() est appelée.

## KieScanner
- Permet de surveiller les modifications des fichiers .drl situés dans le .jar
- Si tous les fichiers de règle sont dans un meme module, permet de mettre à jour les règles sans redémarrer l'application.
- Exemple:
  ```java
  KieServices kieServices = KieServices.Factory.get();
  KieContainer kieContainer = kieServices.newKieContainer(kieServices.newReleaseId("com.sample", "myartifact", "LATEST"));
  KieScanner kieScanner = kieServices.newKieScanner(kieContainer);
  kieScanner.start(10000L); // scan toute les 10 secondes
    ```

## Execution des règles
- Lorsqu'une règle est déclenchée, on sort du fichier .drl
- Pour executer plusieurs règles en même temps, on utilise modify() pour mettre à jour et réévaluées les faits
    - Exemple:
      ```drools
      rule "Customer promoted to silver when makes an order over 200"
      when
        $customer : Customer( category == "Bronze")
        orderInstance : Order( customer == $customer , amount >= 200)
      then
        modify($customer) {
          setCategory("Silver")
        }
      end
      ```

## Rules atributtes
- **no-loop**: éviter la réactivation d'une règle causée par le 'then' de cette MÊME règle.
- **lock-on-active**: éviter la réactivation d'une règle, quelle qu'en soit la cause.
- **salience**: priorité de la règle. Plus la valeur est élevée, plus la règle est prioritaire.
- **agenda-group**: permet de regrouper les règles dans des groupes et de les executer en fonction du groupe.
    - Utilisation de setFocus() requis pour selectionner le groupe à executer.
      ```java
      Agenda agenda = kieSession.getAgenda();
      agenda.getAgendaGroup( "upgrade to silver" ).setFocus();
      agenda.getAgendaGroup( "upgrade to gold" ).setFocus();
      // execute the rules
      ```
    - **auto-focus**: permet de selectionner automatiquement le groupe à executer.
- **activation-group**: meme chose que agenda-group, mais une seule règle du groupe est executée.
- **duration**: permet de définir une durée de vie pour la règle. La règle est retirée de l'agenda après la durée spécifiée.

## Modes d'exécution des règles et sécurité des threads dans le decision engine
Le moteur de décision prend en charge les modes d'exécution des règles suivants, qui déterminent comment et quand le moteur exécute les règles :

- **Mode passif : (Par défaut)** Le moteur de décision évalue les règles avec fireAllRules().
- **Mode actif :** Le moteur de décision évalue les règles avec fireUntilHalt(). halt() doit être appelé pour arrêter l'exécution des règles.

## Gestion des règles : fichier DRL ou XLS ?

### Fichier DRL

#### Avantages
- Syntaxe simple et lisible pour les développeurs.
- Facile à maintenir (versioning/debugging/...)
- Possibilité d'utiliser des expressions conditionnelles et des fonctions.

#### Inconvénients
- Les règles sont codées en dur dans le fichier .drl.
- Les règles sont difficiles à comprendre/créer/modifier/tester pour les non-développeurs.

### Fichier XLS

#### Avantages
- Les règles sont stockées dans un fichier Excel.
- Les règles plus faciles à comprendre pour les non-développeurs.

#### Inconvénients
- Les règles sont plus difficiles à maintenir.
  - pas de versioning
  - pas de debug possible
  - devient rapidement illisible avec beaucoup de règles.

## Logging

On peut activer le logging des règles pour voir les règles qui sont déclenchées et les faits qui sont insérés dans la session.

Exemple dans module decision-table-drools-geode: VehicleStrategy2021
```java
        KieSession kieSession = new DroolsConfig().kieContainer().newKieSession();
        kieSession.addEventListener(new DefaultAgendaEventListener() {
            public void afterMatchFired(AfterMatchFiredEvent event) {
                super.afterMatchFired( event );
                System.out.println( event );
            }
            public void beforeMatchFired(BeforeMatchFiredEvent event) {
                super.beforeMatchFired( event );
                System.out.println( event );
            }
        });
```

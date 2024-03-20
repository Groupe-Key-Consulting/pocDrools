# Drools 

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

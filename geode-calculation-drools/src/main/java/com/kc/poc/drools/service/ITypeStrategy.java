package com.kc.poc.drools.service;

import java.util.Map;

//import info.stif.requeteweb.domain.contract.model.ModelType;
//import info.stif.requeteweb.domain.proposition.Proposition;

/**
 * Type ayant des strategies
 *
 * @param <T>
 */
public interface ITypeStrategy<T> {

    default T getStrategy(Proposition proposition) {
        return getStrategy(proposition.getModel().getType());
    }

    /**
     * Retourner la bonne strategie en supposant que les propositions sont de meme type de modele
     *
     * @param propositions
     * @return
     */
    default T getStrategy(Iterable<Proposition> propositions) {
        return getStrategy(propositions.iterator().next());
    }

    default T getStrategy(ModelType modelType) {
        return strategies().get(modelType);
    }

    Map<ModelType, T> strategies();
}

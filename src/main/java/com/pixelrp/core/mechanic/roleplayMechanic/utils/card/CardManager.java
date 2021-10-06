package com.pixelrp.core.mechanic.roleplayMechanic.utils.card;

import com.pixelrp.core.utils.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardManager {

    private Database cardDB;

    private List<Card> cardList;
    private Map<String, Card> activeCards;

    public CardManager(){
        cardDB = new Database("cards");
        cardList = new ArrayList<>();
        activeCards = new HashMap<>();

        try{
            cardList = (ArrayList<Card>) cardDB.loadDataBase();
        } catch (Exception e){}

    }

    public void createCard(Card card){
        cardList.add(card);
        cardDB.saveDataBase(cardList);
    }

    public void deleteCard(int index){
        cardList.remove(index);
        cardDB.saveDataBase(cardList);
    }

    public List<Card> getCardList(){
        return cardList;
    }

    public Card getCard(int index){
        return cardList.get(index);
    }

    public Database getDatabase(){
        return cardDB;
    }
}

package com.example.teamcity.api.requests;

//Создадим интерфейс для CRUD-операций

public interface Crudinterface {



    //CREATE
    public Object create(Object obj);

    //READ
    public Object get(String id);

    //UPDATE
    public Object update(Object object);

    //DELETE
    public Object delete(String id);




}


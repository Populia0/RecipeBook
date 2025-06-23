package com.recipebook.android.db;

import android.app.Application;

public class RepositoryProvider {
    private static DBRepository repository;

    public static DBRepository getRepository(Application application) {
        if (repository == null) {
            repository = new DBRepository(application);
        }
        return  repository;
    }
}

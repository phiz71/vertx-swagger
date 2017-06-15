package io.swagger.server.api.verticle;

import java.util.List;

import io.swagger.server.api.model.User;
import io.vertx.core.Future;
import rx.Single;

public class UserApiImpl implements UserApi {

    @Override
    public Single<Void> createUser(User body) {
        return Single.just(null);
    }

    @Override
    public Single<Void> createUsersWithArrayInput(List<User> body) {
        return Single.just(null);
    }

    @Override
    public Single<Void> createUsersWithListInput(List<User> body) {
        return Single.just(null);
    }

    @Override
    public Single<Void> deleteUser(String username) {
        return Single.just(null);
    }

    @Override
    public Single<User> getUserByName(String username) {
        return Single.just(null);
    }

    @Override
    public Single<String> loginUser(String username, String password) {
        return Single.just(null);
    }

    @Override
    public Single<Void> logoutUser() {
        return Single.just(null);
    }

    @Override
    public Single<Void> updateUser(String username, User body) {
        return Single.just(null);
    }

}

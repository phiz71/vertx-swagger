package io.swagger.server.api.verticle;

import java.util.List;
import java.util.UUID;

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.model.User;
import rx.Completable;
import rx.Single;

public class UserApiImpl implements UserApi {

    @Override
    public Completable createUser(User body) {
        return Completable.complete();
    }

    @Override
    public Completable createUsersWithArrayInput(List<User> body) {
        return Completable.complete();
    }

    @Override
    public Completable createUsersWithListInput(List<User> body) {
        return Completable.complete();
    }

    @Override
    public Completable deleteUser(String username) {
        return Completable.complete();
    }

    @Override
    public Single<User> getUserByName(String username) {
        return Single.just(new User());
    }

    @Override
    public Single<String> loginUser(String username, String password) {
        return Single.just("");
    }

    @Override
    public Completable logoutUser() {
        return Completable.complete();
    }

    @Override
    public Completable updateUser(String username, User body) {
        return Completable.complete();
    }

    @Override
    public Single<InlineResponseDefault> uuid(UUID uuidParam) {
        return Single.just(new InlineResponseDefault(uuidParam));
    }

}

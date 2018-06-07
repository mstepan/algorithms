package com.max.algs;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;

public final class UserDao {

    private static final int PAGE_SIZE = 10;

    private static final String USERS_PAGINATED_SQL = "SELECT * from user OFFSET %d LIMIT %d;";

    private final List<String> users = new ArrayList<>();

    public UserDao() {
        for (int i = 0; i < 100; ++i) {
            users.add("user-" + i);
        }
    }

    public Observable<String> getUsersPaginated(int pageIndex) {
        return getUsers(pageIndex).concatWith(Observable.defer(
                () -> getUsersPaginated(pageIndex + 1)
        ));
    }

    public Observable<String> getUsers(int pageIndex) {

        final int from = pageIndex * PAGE_SIZE;

        if (from >= users.size()) {
            return Observable.empty();
        }

        return Observable.defer(() ->
                Observable.from(listUsers(pageIndex)));
    }


    private List<String> listUsers(int pageIndex) {

        final int from = pageIndex * PAGE_SIZE;

        System.out.println(String.format(USERS_PAGINATED_SQL, from, PAGE_SIZE));

        final int to = (pageIndex + 1) * PAGE_SIZE;

        return users.subList(from, to);
    }
}

package com.daya.android.permission;

/**
 * Created by Shnoble on 2017. 8. 13..
 */

public class PermissionHelper {

    public static class Builder {
        public Builder setPermissions(String... permissions) {
            return this;
        }

        public PermissionHelper build() {
            return new PermissionHelper();
        }
    }
}

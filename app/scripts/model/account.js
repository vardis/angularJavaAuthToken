'user strict';

/**
 * Holds the details of the backend account that is currently linked to the user.
 * It is using the cookie store for caching the authentication token and for resuming
 * the previous session.
 */
(function () {

    // properties of the active account
    var status = {
        // object with the user properties as returned from the backend
        user: null,

        // the last value of the authentication token
        authToken: null
    };

    var ActiveAccount = function ($cookieStore) {

        return {
            user: function () {
                return status.user;
            },

            authToken: function () {
                return status.authToken;
            },

            signedIn: function (user) {
                status.user = user;
                status.authToken = user.authToken.value;
                $cookieStore.put('ActiveAccount', status);
            },

            signedOut: function () {
                status.authToken = '';
                status.user = null;
                $cookieStore.remove('ActiveAccount');
            },

            isAuthenticated: function () {
                return status.user != null && status.authToken != null;
            }
        };

    };

    ActiveAccount.$inject = ['$cookieStore'];

    angular.module('BackendApis.Model', [])

        .factory('ActiveAccount', ActiveAccount)

        // Run block tries to populate the active account from previous values
        // stored in cookies
        .run(['$cookieStore', function ($cookieStore) {
            var existing = $cookieStore.get('ActiveAccount');
            if (existing != null) {
                status.user = existing.user;
                status.authToken = existing.authToken;
            }
        }
        ]);

}());

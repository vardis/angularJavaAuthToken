'user strict';

/**
 * Defines the BackendApis.Interceptors module. It contains interceptors
 * for HTTP requests and responses.
 * There following interceptors are currently defined:
 * <ul>
 *  <li>loginInterceptor
 *      Intercepts responses and will redirect to the login page upon a
 *      401 Non-Authorized response.
 *  </li>
 *  <li>authTokenInterceptor
 *      Intercepts outgoing requests in order to add the authentication
 *      token in the HTTP headers.
 *  </li>
 */
(function () {

    /**
     * Intercepts responses and checks the response code. If the code indicates
     * an authorization error (code 401), it changes the location to the login
     * page.
     */
    var loginInterceptor = function ($injector, $q, $location) {
        return function (responsePromise) {
            return responsePromise.then(null, function (response) {
                if (response.status === 401) {
                    $location.path('/login');
                }
                return $q.reject(response);
            });
        };
    };

    loginInterceptor.$inject = ['$injector', '$q', '$location'];


    /**
     * Intercepts outgoing requests and adds the authentication cookie
     * in the HTTP headers.
     *
     * @param ActiveAccount requires an ActiveAccount to retrieve the authentication token
     * @returns {{request: request interceptor, response: response interceptor}}
     */
    var authTokenInterceptor = function(ActiveAccount, Constants) {
        return {
            request: function(config) {
                var authToken = ActiveAccount.authToken();
                if (authToken) {
                    config.headers = config.headers || {};
                    config.headers[Constants.AUTH_TOKEN_HEADER] = authToken;
                }
                return config;
            }

        }
    };

    authTokenInterceptor.$inject = ['ActiveAccount', 'Constants'];

    angular.module('BackendApis.Interceptors', [])

        .factory('loginInterceptor', loginInterceptor)

        .factory('authTokenInterceptor', authTokenInterceptor)

        // registers the interceptors
        .config(['$httpProvider', function ($httpProvider) {
            $httpProvider.responseInterceptors.push('loginInterceptor');
            $httpProvider.interceptors.push('authTokenInterceptor')
        }]);

}());
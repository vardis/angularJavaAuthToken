'use strict';

/**
 * Defines the main application module.
 * Routing and initialization run blocks are also defined here.
 */
(function () {

    angular.module('BackendApis.App', [
        'ngCookies',
        'ngResource',
        'ngSanitize',
        'ngRoute',
        'BackendApis.Services',
        'BackendApis.Interceptors',
        'BackendApis.Model'
    ])

    // Stores all application constants in a dedicated module
    .factory('Constants', ['$location', function ($location) {
        var baseUrl = $location.protocol() + '://' + $location.host();
        if ($location.port()) {
          appUrl += ':' + $location.port();          
        }
        var appUrl = baseUrl + '/app';
        var apiUrl = appUrl + '/api';

        return {
            AUTH_TOKEN_HEADER: 'X-BackendApis-Auth-Token',
            APP_URL: appUrl,
            API_URL: apiUrl,
            OPEN_URLS: ['/']
        }
    }])

    // Routing
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when('/login', {
                templateUrl: 'views/login.html',
                controller: 'LoginCtrl'
            })
            .when('/restricted', {
                templateUrl: 'views/restricted.html'
            })
            .otherwise({
                redirectTo: '/'
            });
    })
    
    // sets up CORS    
    .run(['$http', function ($http) {
        $http.defaults.useXDomain = true;
        delete $http.defaults.headers.common['X-Requested-With'];
    }])
    
    // stores the ActiveAccount on the scope, subsequent code should deal only
    // with this instance    
    .run(['$rootScope', 'ActiveAccount', function ($rootScope, ActiveAccount) {
        $rootScope.ActiveAccount = ActiveAccount;
    }]);

}());
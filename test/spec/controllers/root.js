'use strict';

describe('Controller: RootCtrl', function () {

  // load the controller's module
  beforeEach(module('BackendApis.App'));

  var RootCtrl,
  scope,
  location;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope, $location) {
    scope = $rootScope.$new();
    location = $location;
    RootCtrl = $controller('RootCtrl', {
      '$scope': scope,
      '$location': location
    });
  }));

  it('should change the location to the given argument', function () {
    scope.go('/#/nowhere');
    expect(location.path()).toBe('/#/nowhere');
  });

  it('should redirect to the login page when requesting a restricted page', function () {
    scope.go('/#/restricted');
    expect(location.path()).toBe('/#/login');
  });
});

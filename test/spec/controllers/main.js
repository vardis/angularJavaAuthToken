'use strict';

describe('Controller: MainCtrl', function () {

  // load the controller's module
  beforeEach(module('BackendApis.App'));

  var MainCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MainCtrl = $controller('MainCtrl', {
      $scope: scope
    });
  }));

  it('should report that initially the user is not authenticated', function () {
    expect(scope.isLoggedIn()).toBe(false);
  });
});

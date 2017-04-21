/**
 * Created by IMishev on 8/11/2016.
 */
angular.module('app').controller('activeUserCtrl',
    [ '$rootScope', '$scope', function($rootScope, $scope) {
        $scope.activeUser = $rootScope.activeUser;

    } ]);

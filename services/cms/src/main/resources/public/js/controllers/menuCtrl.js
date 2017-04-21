angular.module('app').controller('menuCtrl',
        [ '$rootScope', '$scope', '$timeout', function($rootScope, $scope, $timeout) {

            $scope.me = "Profile";
            $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
                $('#menu-content > li > ul > li > ul').hide();
                if (toParams.workspaceId) {
                    $('#submenu-' + toParams.workspaceId).show();
                }
            })

            $scope.$on('communities.set.changed', function(event, value) {
                $scope.getMyWorkspaces();
            })

            $scope.getMyWorkspaces = function() {

            };
            $scope.getMyWorkspaces();
            $scope.cssHeightFix = function() {
                height = $('#navBar').height();
                height = height + 120;
                $('#board').css('min-height', height);
            }
        } ]);

/**
 * Created by mmanchev on 6/23/2016.
 */
angular.module('app').factory('UserService', ['$rootScope', '$log', '$q', 'GeneralService', function($rootScope, $log, $q, GeneralService) {
    var UserService =  {
        loadUser : function() {
            var deferred = $q.defer();
            var username = null;
            GeneralService.get('security/user').then(function(response) {
                username = response.name;
            });

            GeneralService.get('user/users/logged').then(function(response) {
                var user = response;
                if (user) {
                    sessionStorage.setItem("sessionUser", JSON.stringify(user)) ;
                    deferred.resolve(user);
                }
            });
            return deferred.promise
        }
    };
    return UserService;
} ]);

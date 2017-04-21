/**
 * Created by mmanchev on 6/23/2016.
 */
angular.module('app').factory('IdMapService', ['$rootScope', '$log', '$q', 'GeneralService', function($rootScope, $log, $q, GeneralService) {
    var IdMapService =  {
        getWeakId : function(macAddress, userId) {
            var deferred = $q.defer();
            var mapIdUrl = 'idmap/idMaps?companyId=' + $rootScope.companyId;
            if (typeof macAddress != 'undefined') {
                mapIdUrl += '&macAddress=' + window.encodeURIComponent(macAddress);
            } else if (typeof userId != 'undefined') {
                mapIdUrl += '&userId=' + userId;
            }

            GeneralService.get(mapIdUrl).then(function(response) {
                if (typeof response != 'undefined' && response != null) {
                    deferred.resolve(response);
                }
            });
            return deferred.promise
        },
        updateWeakId : function(weakId, userId, macAddress) {
            var deferred = $q.defer();
            var url = 'idmap/idMaps';
            var idMap = {
                    "macAddress" : macAddress,
                    "weakId" : weakId,
                    "userId" : userId,
                    "companyId" : $rootScope.companyId
            };
            GeneralService.save(idMap, url).then(function(response) {
                if (typeof response != 'undefined' && response != null) {
                    deferred.resolve(response);
                }
            });
            return deferred.promise
        }
    };
    return IdMapService;
} ]);

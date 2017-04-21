/**
 * Created by mmanchev on 6/23/2016.
 */
angular.module('app').factory('JabberGuestService', [ '$log', '$q', 'GeneralService', function($log, $q, GeneralService) {
    var JabberGuestService = {
        getJabberSession : function(userName, userId) {

            /**
             * @requestPath should be unique. That's why the time stamp is a
             *              workaround here. It could contain the userId and
             *              productId and probably some unique identifier. This
             *              could be done in some pattern, so we can easily
             *              fetch the user and the reviewing product from where
             *              he called the support.
             */
            var data = {
                "requestPath" : Math.random().toString(36).substring(15) + "-" + new Date().getTime(),
                "callerName" : userId
            };

            var url = "collaboration/jabber";

            var deferred = $q.defer();
            var jabberGuestLink = "";
            GeneralService.save(data,url).then(function(response) {
                var obj = response.toJSON();
                jabberGuestLink = obj['link'];
                console.log("Link: " +  jabberGuestLink);
                $log.info('Jabber Guest Link saved: ' + jabberGuestLink);
                deferred.resolve(jabberGuestLink);
            }, function(errorMsg) {
                $log.error('Error creating item: ' + errorMsg);
                deferred.reject();
            });
            return deferred.promise;

        }
    };
    return JabberGuestService;
} ]);

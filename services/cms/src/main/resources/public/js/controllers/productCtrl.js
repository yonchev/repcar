/**
 * Created by mmanchev on 6/23/2016.
 */
angular.module('app').controller(
        'productCtrl',
        [ '$scope', '$stateParams', '$log', 'JabberGuestService', 'GeneralService',
                function($scope, $stateParams, $log, JabberGuestService, GeneralService) {

                    var productId = $stateParams.productId;
                    var url = 'product/products/' + productId + "?companyId=";

                    var user = JSON.parse(sessionStorage.getItem("sessionUser"));

                    GeneralService.get(url + $scope.companyId).then(function(response) {
                        $scope.product = response;

                        if ($scope.weakId && $scope.product && $scope.product.productId) {
                            var data = {
                                "entityId" : $scope.weakId,
                                "entityType" : "user",
                                "event" : "view",
                                "targetEntityId" : "i" + $scope.product.productId,
                                "targetEntityType" : "item",
                                "properties" : {
                                    "weakId" : [$scope.weakId]
                                }
                            };
                            var userDataUrl = '/user-data/userdata';
                            GeneralService.save(data, userDataUrl).then(function(response) {
                                $log.info(response);
                            });
                        } else {
                            $log.error("User action event could not be logged. Missing data for event details.")
                        }

                        // Get a new specific Jabber Guest Link for the current user and the reviewing product
                        if (user) {
                            JabberGuestService.getJabberSession(user.userFirstName + " " + user.userLastName, user.userId).then(function(response) {
                                $scope.jabberGuestLink = response;
                            });
                        }

                    });

                    $scope.buyProduct = function(productId) {
                        if ($scope.weakId && productId) {
                            var data = {
                                "entityId" : $scope.weakId,
                                "entityType" : "user",
                                "event" : "buy",
                                "targetEntityId" : "i" + productId,
                                "targetEntityType" : "item",
                                "properties" : {
                                    "weakId" : [$scope.weakId]
                                }
                            };
                        } else {
                            $log.error("User action event could not be logged. Missing data for event details.")
                        }
                            var userDataUrl = '/user-data/userdata';
                            GeneralService.save(data, userDataUrl).then(function(response) {
                                $log.info(response);
                            });
                    }

                } ]);

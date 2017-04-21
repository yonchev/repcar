/*
 * Copyright RepCar AD 2017
 */
angular.module('app').controller('userProfileCtrl',
        [ '$scope', '$timeout', '$stateParams', 'GeneralService', 'RecommendedProductsService', function($scope, $timeout, $stateParams, GeneralService, RecommendedProductsService) {

            var dialog = $("#meetingInfoModal");
            dialog.hide();

            var snackbarContainer = document.querySelector('#meetingInfo-snackbar');

            var showSnack = function(msg) {
                var data = {
                        message: msg,
                        timeout: 3000,
                        actionHandler: '',
                        actionText: ''
                      };
                snackbarContainer.MaterialSnackbar.showSnackbar(data);
            }

            var showError = function() {
                showSnack("No Selected product found for this user and no meeting will be created.");
            }

            var url = 'idmap/idMaps?userId=' + $stateParams.userId + '&companyId=' + sessionStorage.getItem("sessionCompanyId");
            GeneralService.get(url).then(function(response) {
                if (typeof response != 'undefined' && response != null) {
                    $scope.userProfile = response._embedded.idmaps[0].user;
                    $scope.userProfile.userId = $stateParams.userId;
                    $scope.userProfile.companyId = sessionStorage.getItem("sessionCompanyId");
                    var url = "/user-data/userdata/" + response._embedded.idmaps[0].weakId;
                    RecommendedProductsService.get(url).then(function(response) {
                        if (typeof response != 'undefined' && response != null) {
                            $scope.products = [ response ];
                            $scope.hasProducts = true;
                        } else {
                            $scope.hasProducts = false;
                        }
                    });
                } else {
                    $scope.userProfile = '{}';
                }
            });

            $scope.showCreateMeetingInfo = function() {
                dialog.show();
            };

            $scope.hideCreateMeetingInfo = function() {
                dialog.hide();
            };

            $scope.createMeetingInfo = function() {
                var userId = $scope.userProfile.userId;
                var companyId = $scope.userProfile.companyId;
                var operator  = JSON.parse(sessionStorage.getItem("sessionUser"));
                var operatorId = operator.userId;

                GeneralService.get('idmap/idMaps?userId=' + userId + '&companyId=' + companyId).then(function(response) {
                    var userWeakId = response._embedded.idmaps[0].weakId;
                    var selectedProductByUserUrl = 'user-data/userdata/' + userWeakId;
                    GeneralService.get(selectedProductByUserUrl).then(function(response) {
                        var productId = response.productId;
                        if (productId !== undefined || productId != null) {
                            var data = {
                                    "title": $scope.title,
                                    "notes": $scope.notes,
                                    "user" : { "userId": userId, "company" : { "companyId" : companyId} },
                                    "operator": { "userId": operatorId, "company" : { "companyId" : companyId} },
                                    "product" : {"productId": productId, "company" : { "companyId" : companyId} }
                            };

                            var url = 'meeting/meetings';
                            GeneralService.save(data, url).then(function(response) {
                                if (typeof response != 'undefined' && response != null &&
                                        typeof response.meetingInfoId != 'undefined') {
                                    showSnack("The record is saved successfully!");
                                    $scope.lastCreatedMeetingInfo = window.location.origin + "/#/meetingInfo/" + response.meetingInfoId;
                                    $scope.hideCreateMeetingInfo();
                                }
                            });
                        } else {
                            showError();
                        }
                    }, function(error) {
                        showError();
                    });
                });
                
            };
        } ]);

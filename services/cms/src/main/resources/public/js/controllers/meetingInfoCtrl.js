/*
 * Copyright RepCar AD 2017
 */
angular.module('app').controller(
        'meetingInfoCtrl',
        [ '$scope', '$timeout', '$stateParams', 'GeneralService',
                function($scope, $timeout, $stateParams, GeneralService) {
                    var meetingUrl = 'meeting/meetings/';

                    meetingUrl = meetingUrl.concat($stateParams.meetingInfoId);
                    GeneralService.get(meetingUrl).then(function(response) {
                        if (typeof response != 'undefined' && response != null) {
                            $scope.meetingInfo = response;
                        } else {
                            $scope.meetingInfo = '{}';
                        }
                        if (typeof $scope.meetingInfo.creationDate != 'undefined') {
                            // Create a new JavaScript Date object based on the
                            // timestamp
                            // multiplied by 1000 so that the argument is in
                            // milliseconds, not seconds.
                            var date = new Date($scope.meetingInfo.creationDate);
                            $scope.meetingInfo.formattedCreateDate = date;
                        }
                    });

                } ]);

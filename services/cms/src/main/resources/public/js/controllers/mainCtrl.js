angular.module('app').controller('mainCtrl', [
    '$scope',
    '$rootScope',
    '$resource',
    '$http',
    'UserService',
    'IdMapService',
    '$state',
    '$interval',
    'ActiveUsersService',
    'GeneralService',
    function ($scope, $rootScope, $resource, $http, UserService, IdMapService, $state, $interval, ActiveUsersService, generalService) {

    var user = JSON.parse(sessionStorage.getItem('sessionUser'));

    var checkWeakId = function () {
        var storedWeakId = localStorage.getItem('WEAK_ID');
        var macAddr;
        console.log("storedWeakId is " + storedWeakId);
        if (typeof macAddress != 'undefined') {
            macAddr = macAddress.getMacAddress();
            console.log("macAddress is: " + macAddress.getMacAddress());
        }
        var userId;
        if (user != null) {
            userId = user.userId;
        }
        if (!storedWeakId) {
            IdMapService.getWeakId(macAddr, userId).then(function(response) {
                if (typeof response != 'undefined' && response != null) {
                    var weakId = response._embedded.idmaps[0].weakId;
                    localStorage.setItem('WEAK_ID', weakId);
                    $scope.weakId = weakId;
                }
            });
        } else {
            var loggedInUser = sessionStorage.getItem("sessionUser");
            if (!loggedInUser) {
                $scope.weakId = storedWeakId;
            } else {
                IdMapService.updateWeakId(storedWeakId, JSON.parse(loggedInUser).userId, macAddr).then(function(response) {
                    $scope.weakId = response.weakId;
                    localStorage.setItem('WEAK_ID', response.weakId);
                });
            }
        }
    };

    $scope.doSkipVideo = function(doSkip) {
        var skip = localStorage.getItem("skipVideo");
        if (!skip) {
            skip = doSkip;
            localStorage.setItem("skipVideo", true);
        }
        if (!skip) {
            skip = false;
        }
        $scope.skipVideo = skip;
    };

    var init = function () {
        var accessToken = sessionStorage.getItem('access_token');
        var companyId = sessionStorage.getItem("sessionCompanyId");
        $scope.companyId = companyId;
        $rootScope.companyId = companyId;
        if (accessToken) {
            $scope.logoutButton = true;
            $http.defaults.headers.common['Authorization'] = 'Bearer ' + accessToken;
            if (!user) {
                UserService.loadUser();
            }
        } else {
            $scope.logoutButton = false;
            $state.go('productList');
        }
        $scope.user = user;
        checkWeakId();
        $scope.doSkipVideo();
    };
    init();

    //Only operator must be able to use cmx service.
    if(user && (user.userRole === "ROLE_OPERATOR" || user.userRole === "ROLE_ADMIN")){

        $rootScope.activeUsersClicked = false;

        $scope.activeUsersClicked = function(){

            var activeUserTab = $('#activeUsersTab')[0];

            if(activeUserTab){

                if((!$(activeUserTab).hasClass("is-active"))){
                    $rootScope.activeUsersClicked = true;
                    activeUserTab.click();
                }
            }else{
                $rootScope.activeUsersClicked = true;
            }

        };

        function checkActive(){
            return $interval(getActiveMacs , 10000);
        }

        checkActive();

        function checkNewActive(arr1, arr2) {
            if(arr1.length !== arr2.length)
                return true;
            for(var i = arr1.length; i--;) {
                if(arr1[i].weakId !== arr2[i].weakId)
                    return true;
            }
            return false;
        }

        function getActiveMacs(){

            var activeUrl = '/cmx/macs/active' + '?' + 'companyId=' + $scope.companyId;

            ActiveUsersService.get(activeUrl).then(function(response){
                var regUsersIds = [];
                var unregisteredUsersIds = [];

                if(typeof $scope.countActive == 'undefined'){
                    $scope.countActive = response.length;
                }

                if(typeof $scope.activeRegisteredUsers == 'undefined'){
                    $scope.activeRegisteredUsers = regUsersIds;
                }

                if(typeof $scope.activeUnregisteredUsers == 'undefined'){
                    $scope.activeUnregisteredUsers = unregisteredUsersIds;
                }

                if(typeof  $scope.activeUsers != 'undefined'){
                    if(checkNewActive($scope.activeUsers, response)){
                        $scope.activeRegisteredUsers = regUsersIds;
                        $scope.activeUnregisteredUsers = unregisteredUsersIds;
                        $scope.countActive =  response.length;
                    }
                }

                $scope.activeUsers = response;
                for(var i = 0; i<response.length; i++){

                    const regUserId = {};
                    if(response[i].userId != 0 && response[i].userId != null){
                        regUserId.userIdMap = response[i];
                        var userUrl = '/user/users/' + response[i].userId;

                        generalService.get(userUrl).then(function(response){
                            regUserId.userData = response;
                            regUsersIds.push(regUserId);
                        });
                    }else{
                        const unregisteredUsersId = {
                            userData: {
                                userName: "Unknown"
                            }
                        };
                        unregisteredUsersId.userIdMap = response[i];
                        unregisteredUsersIds.push(unregisteredUsersId);
                    }
                }

            });
        }

    }

}]);

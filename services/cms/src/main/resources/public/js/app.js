var app = angular.module('app', [ 'ngResource', 'ui.router', 'app.directives.backgroundDir' ]);
app.config([ '$urlRouterProvider', '$stateProvider', '$httpProvider',
        function($urlRouterProvider, $stateProvider, $httpProvider) {
            // $urlRouterProvider.otherwise('/messageBoard');
            // $httpProvider.interceptors.push('httpInterceptor');
            sessionStorage.setItem("sessionCompanyId", 1);
            $stateProvider.state('productList', {
                url : '/productList',
                templateUrl : 'view/product/productList.html',
                controller : 'productListCtrl'
            }).state('landing', {
                url : '',
                templateUrl : 'view/product/productList.html',
                controller : 'productListCtrl'
            }).state('error', {
                url : '/error',
                templateUrl : 'view/landing.html',
                controller : 'landingCtrl'
            }).state('home', {
                url : '/home',
                templateUrl : 'view/home.html',
                controller : 'homeCtrl'
            }).state('login', {
                url : '/login',
                templateUrl : 'view/login.html',
                controller : 'loginCtrl'
            }).state('logout', {
                templateUrl : 'view/login.html',
                controller : 'logoutCtrl'
            }).state('product', {
                url : '/product/{productId}',
                templateUrl : 'view/product/product.html',
                controller : 'productCtrl'
            }).state('createProduct', {
                url : '/createProduct',
                templateUrl : 'view/product/createProduct.html',
                controller : 'createProductCtrl'
            }).state('createCompany', {
                url : '/createCompany',
                templateUrl : 'view/company/createCompany.html',
                controller : 'createCompanyCtrl'        
            }).state('operatorConsole', {
                url : '/operatorConsole',
                templateUrl : 'view/operator/operatorConsole.html',
                controller : 'operatorConsoleCtrl'
            }).state('meetingInfo', {
                url : '/meetingInfo/{meetingInfoId}',
                templateUrl : 'view/operator/meetingInfo.html',
                controller : 'meetingInfoCtrl'
            }).state('userProfile', {
                url : '/userProfile/{userId}',
                templateUrl : 'view/operator/userProfile.html',
                controller : 'userProfileCtrl'
            }).state('phoneSettings', {
                url : '/phoneSettings',
                templateUrl : 'view/operator/userProfile.html',
                controller : 'userProfileCtrl'
            }).state('jabberSettings', {
                url : '/jabberSettings',
                templateUrl : 'view/operator/jabberSettings.html',
                controller : 'jabberSettingsCtrl'
            }).state('activeUser', {
                url : '/activeUser',
                templateUrl : 'view/operator/activeUser.html',
                controller : 'activeUserCtrl'
            });
            /*
             * $translateProvider.useStaticFilesLoader({ prefix:
             * '../Netspace-1.0/js/languages/', suffix: '.json' });
             */
            // $translateProvider.preferredLanguage('en_US');
            $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
        } ]);

app.run(function($rootScope){
    $rootScope.$on('$viewContentLoaded', function(event, next) {
        componentHandler.upgradeAllRegistered();
    });
    var user = JSON.parse(sessionStorage.getItem('sessionUser'));
 // Load Jabber Web Settings
    if(user!=null){
        if(user.userRole==='ROLE_ADMIN' || user.userRole=='ROLE_OPERATOR'){
            load_scripts();
        }
    }

    function load_scripts() {
        type = 'text/javascript';

        var ciscobase = document.createElement('script');
        ciscobase.src = 'js/third-party/jabber-web/ciscobase.js';
        ciscobase.type = type;
        document.body.appendChild(ciscobase);

        var cwic = document.createElement('script'); // use global document since Angular's $document is weak
        cwic.src = 'js/third-party/jabber-web/cwic.js';
        cwic.type = type;
        document.body.appendChild(cwic);

        var settings = document.createElement('script');
        settings.src = 'js/third-party/jabber-web/Settings.js';
        settings.type = type;
        document.body.appendChild(settings);

        var toast = document.createElement('script');
        toast.src = 'js/third-party/jabber-web/Toast.js';
        toast.type = type;
        document.body.appendChild(toast);
    }
});

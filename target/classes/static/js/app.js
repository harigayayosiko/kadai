(function(){
    'use strict';
    angular.module('rcw',['ngResource']).config(function($locationProvider){
        $locationProvider.html5Mode(true);
    });
}());

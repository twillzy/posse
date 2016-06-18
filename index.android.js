import React, { Component } from 'react';
import { AppRegistry, StyleSheet, Text, View, Navigator } from 'react-native';
import Login from './App/Views/login';

var RouteMapper = function(route, navigationOperations, onComponentRef) {
  if (route.name === 'login') {
    return <Login navigator={navigationOperations}/>
  }
};

class Posse extends Component {
  render() {
    var initialRoute = {name: 'login'};

    return (
      <Navigator
  	    initialRoute={initialRoute}
  	    configureScene={() => Navigator.SceneConfigs.FadeAndroid}
  	    renderScene={RouteMapper}
  	    />
    );
  }
}

AppRegistry.registerComponent('Posse', () => Posse);

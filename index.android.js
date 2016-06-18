import React, { Component } from 'react';
import { AppRegistry, StyleSheet, Text, View, Navigator } from 'react-native';
import Login from './App/Views/login';
// import Posse from './App/Views/posse';

var RouteMapper = function(route, navigationOperations, onComponentRef) {
  if (route.name === 'login') {
    return <Login navigator={navigationOperations} {...route.passProps}/>
  } else if (route.name === 'posse') {
    return <Posse navigator={navigationOperations} fbid={route.fbid} {...route.passProps}/>
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

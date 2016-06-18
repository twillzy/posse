import React, { Component } from 'react';
import { LoginButton, AccessToken, GraphRequest, GraphRequestManager } from 'react-native-fbsdk';
import { StyleSheet, Text, View, Navigator, Image } from 'react-native';
import FacebookLogin from './../NativeModules/FacebookLogin';

export default class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      fbid: "",
      accessToken: "",
      firstName: "",
      lastName: "",
      gender: ""
    };
  }

  _navigate(property){
    this.props.navigator.push({
      name: property,
      fbid: this.state.fbid,
    });
  }

  render() {
    return (
      <View style={styles.container}>
        <Image
          style={styles.logo}
          source={require('./../Images/posse_logo.png')}/>
        <LoginButton
          publishPermissions={["publish_actions"]}
          onLoginFinished={
            (error, result) => {
              if (error) {
                alert("login has error: " + result.error);
              } else if (result.isCancelled) {
                alert("login is cancelled.");
              } else {
                AccessToken.getCurrentAccessToken().then(
                  (data) => {
                    this.setState({accessToken: data.accessToken.toString()});
                    const responseCallback = ((error, result) => {
                      const response = {};
                      if (error) {
                            response.ok = false;
                            response.error = error;
                            return(response);
                      } else {
                            response.ok = true;
                            response.json = result;
                            this.setState({fbid: result.id, firstName: result.first_name, lastName: result.last_name, gender: result.gender});
                            handleFacebookAccessToken(this.state.accessToken, this.state.fbid, this.state.firstName, this.state.lastName, this.state.gender);
                            return(response);
                      }
                    });

                    const profileRequestParams = {
                      fields: {
                          string: 'id, email, first_name, last_name, gender'
                      }
                    };

                    const profileRequestConfig = {
                      httpMethod: 'GET',
                      version: 'v2.5',
                      parameters: profileRequestParams,
                      accessToken: data.accessToken.toString()
                    };

                    const profileRequest = new GraphRequest(
                      '/me',
                      profileRequestConfig,
                      responseCallback,
                    );

                    new GraphRequestManager().addRequest(profileRequest).start();
                  }
                );
              }
            }
          }
          onLogoutFinished={() => {
            handleSignout().then(() => {
              console.log("Signed out");
              console.log(this._navigate("posse"));
            })
          }}/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  logo: {
    width: 400,
    height: 400,
    marginBottom: 5
  }
});

async function handleFacebookAccessToken(token, fbid, firstName, lastName, gender) {
  try {
    await FacebookLogin.handleFacebookAccessToken(token, fbid, firstName, lastName, gender);
  } catch (e) {
    console.error(e);
  }
}

async function handleSignout() {
  try {
    await FacebookLogin.handleSignout();
  } catch (e) {
    console.error(e);
  }
}

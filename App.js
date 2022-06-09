import React, {Component} from 'react';
import { StyleSheet, Button, View, SafeAreaView, Text, Alert } from 'react-native';

const {RNHelpshift} = NativeModules; 

import { 
  NativeModules,
  NativeEventEmitter,
  requireNativeComponent
} from 'react-native';
import PropTypes from 'prop-types';
class Helpshift extends React.Component {
  render() {
    RNHelpshift.init(appId, domain);
    return (    
    <SafeAreaView style={styles.container}>
      <View>
        <Button
          title="Show FAQ"
          onPress={() =>          
            RNHelpshift.showFAQs()
            //Alert.alert('Show Toast')        
          }
        />
      </View>
      <Separator /> 
      <View>
        <Button
          title="Show FAQ By ID"
          onPress={() =>           
            RNHelpshift.showFAQbyId()                 
          }
        />
      </View>
      <RNTHelpshift {...this.props} />  
      <Separator />    
    </SafeAreaView>    
    );
  }
}

const domain = 'ravi-demo.helpshift.com';
const iosAppId = 'ravi-demo_platform_20220331005029716-39b80be81e0cfb1';
const androidAppId = 'ravi-demo_platform_20220331005029798-1e2a4b59980151a';

const appId = Platform.select({ ios: iosAppId, android: androidAppId })

Helpshift.init = (appId, domain) => RNHelpshift.init(appId, domain);

// TODO: Rerender Helpshift view on iOS if using <Helpshift/>
Helpshift.login = user => RNHelpshift.login(user)

//Helpshift.logout = user => RNHelpshift.logout();

Helpshift.showConversation = () => RNHelpshift.showConversation();

Helpshift.showFAQs = () => RNHelpshift.showFAQs();

var RNTHelpshift = requireNativeComponent('RNTHelpshift', Helpshift);

const Separator = () => (
  <View style={styles.separator} />
);

const styles = StyleSheet.create({
  container: {    
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'center',
    marginHorizontal: 16,
  },
  title: {
    textAlign: 'center',
    marginVertical: 8,
  },
  fixToText: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  separator: {
    marginVertical: 8,
    borderBottomColor: '#737373',
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
});

export default Helpshift;

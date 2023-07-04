import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-awesome-android-123' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const AwesomeAndroid123 = NativeModules.AwesomeAndroid123
  ? NativeModules.AwesomeAndroid123
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return AwesomeAndroid123.multiply(a, b);
}

export function setEventNameProperties(eventName: string) {
  return AwesomeAndroid123.setEventNameProperties(eventName);
}

export function setEventProperties(eventName: string, eventProp: Map<any, any>) {
  return AwesomeAndroid123.setEventProperties(eventName, eventProp);
}

export function setInstantEventNameProperties(eventName: string) {
  return AwesomeAndroid123.setInstantEventNameProperties(eventName);
}

export function setInstantEventProperties(eventName: string, eventProp: Map<any, any>) {
  return AwesomeAndroid123.setInstantEventProperties(eventName, eventProp);
}

export function setUserIdentities(userProp: Map<any, any>) {
  return AwesomeAndroid123.setUserIdentities(userProp);
}

export function hashUserIdentitiesAndSet(userProp: Map<any, any>, useOldConfig: Boolean) {
  return AwesomeAndroid123.hashUserIdentitiesAndSet(userProp, useOldConfig);
}

export function setUserProperties(userProp: Map<any, any>) {
  return AwesomeAndroid123.setUserProperties(userProp);
}

export function setPageProperties(userProp: Map<any, any>) {
  return AwesomeAndroid123.setPageProperties(userProp);
}

export function unsetUserIdentities() {
  return AwesomeAndroid123.unsetUserIdentities();
}

export function deInitiate() {
  return AwesomeAndroid123.deInitiate();
}


export function reInitialize() {
  return AwesomeAndroid123.reInitialize();
}

export function setConsent(consent: Map<any, any>) {
  return AwesomeAndroid123.setConsent(consent);
}

export function addAskForConsentListener(cb: any) {
  return AwesomeAndroid123.addAskForConsentListener(cb);
}

export function getZI(cb: any) {
  return AwesomeAndroid123.getZI(cb);
}

export function resetZI() {
  return AwesomeAndroid123.resetZI();
}


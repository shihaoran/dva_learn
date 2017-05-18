/**
 * Created by shi on 2017/5/16.
 */
import { Platform } from 'react-native';
import DeviceInfo from 'react-native-device-info'

module.exports = {
  apiEndPoint: 'https://mop.baidu.com/rest/mobile/api/1.0.0',
  appKey: 'uuapclient-35-N2dzyodwt4o3pYFHo7or',
  platform: (Platform.OS === 'ios') ? 'ios' : 'android',
  clientType: 'module',
  uuid: DeviceInfo.getUniqueID(),


  actionGetMenuList: 'app/getMenuList',
  actionGetMeasureList: 'app/getMeasureList',
  actionGetMeasureFavorites: 'app/getMeasureFavorites',
  actionUpdateMeasureFavorites: 'app/updateMeasureFavorites',
  actionUpdateUserFeedback: 'app/updateUserFeedback',
};
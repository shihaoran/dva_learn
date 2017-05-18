/**
 * Created by shi on 2017/5/16.
 */
import {
  apiEndPoint,
  appKey,
  platform,
  clientType,
  uuid,
  actionGetMenuList,
  actionGetMeasureFavorites,
  actionGetMeasureList,
  actionUpdateMeasureFavorites,
  actionUpdateUserFeedback,
} from '../constants/environment';
import request from '../utils/request';

const head = {
  Appkey: appKey,
  Client: platform,
  ClientType: clientType,
  UUID: uuid,
}

export function getMenuList(user, token) {
  return request(`${apiEndPoint}?at=${token}`, {
    method: 'POST',
    body: JSON.stringify({
      Header: {
        ...head,
        ST: token,
        User: user,
      },
      Body: {
        Action: actionGetMenuList,
        ResponseType: 'json',
        RequestParam: {
          type: 'app',
        }
      },
    })
  });
}

export function getMeasureList(user, token, date, offset) {
  return request(`${apiEndPoint}?at=${token}`, {
    method: 'POST',
    body: JSON.stringify({
      Header: {
        ...head,
        ST: token,
        User: user,
      },
      Body: {
        Action: actionGetMenuList,
        ResponseType: 'json',
        RequestParam: {
          type: 'app',
        }
      },
    })
  });
}

export function patch(id, values) {
  return request(`/api/users/${id}`, {
    method: 'PATCH',
    body: JSON.stringify(values),
  });
}

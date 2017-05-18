import { createAction, NavigationActions } from '../utils'

import * as uuapService from '../services/uuap'

export default {
  namespace: 'app',
  state: {
    fetching: false,
    login: false,
    userName: '',
  },
  reducers: {
    setUserName(state, { payload }) {
      return { ...state, userName: payload.login.username }
    }
  },
  effects: {
    *login({ payload }, { call, put }) {
      const login = yield call(uuapService.login, payload)
      if (login.status) {
        yield put(createAction('setUserName')({ login }))
      }
    },
    *getTicket({ payload }, { call, put }) {
      const login = yield call(uuapService.getTicket, payload)
    },
    *forceUpdateToken({ payload }, { call, put }) {
      const login = yield call(uuapService.forceUpdateToken, payload)
    },
  },
}

import { createAction, NavigationActions } from '../utils'
import * as authService from '../services/auth'
import * as uuapService from '../services/uuap'

export default {
  namespace: 'app',
  state: {
    fetching: false,
    login: false,
    userName: '',
  },
  reducers: {
    loginStart(state, { payload }) {
      return { ...state, ...payload, fetching: true }
    },
    loginEnd(state, { payload }) {
      return { ...state, ...payload, fetching: false }
    },
    setUserName(state, { payload }) {
      return { ...state, userName: payload.login.username }
    }
  },
  effects: {
    *login1({ payload }, { call, put }) {
      yield put(createAction('loginStart')())
      const login = yield call(authService.login, payload)
      if (login) {
        yield put(
          NavigationActions.reset({
            index: 0,
            actions: [NavigationActions.navigate({ routeName: 'Main' })],
          }),
        )
      }
      yield put(createAction('loginEnd')({ login }))
    },
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

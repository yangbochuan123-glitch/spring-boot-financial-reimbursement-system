import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: JSON.parse(localStorage.getItem('user')) || null
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
      localStorage.setItem('user', JSON.stringify(user))
    },
    CLEAR_USER(state) {
      state.user = null
      localStorage.removeItem('user')
    }
  },
  actions: {
    login({ commit }, user) {
      commit('SET_USER', user)
    },
    logout({ commit }) {
      commit('CLEAR_USER')
    }
  },
  getters: {
    isLoggedIn: state => !!state.user,
    currentUser: state => state.user
  }
}) 
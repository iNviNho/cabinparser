import {createApp} from 'vue/dist/vue.esm-bundler'
import App from './App.vue'
import { createVfm } from 'vue-final-modal'
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from './App.vue'

const app = createApp(App);
const vfm = createVfm()

const routes = [
  { path: '/', component: HomeView, name: 'home' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

app
    .use(vfm)
    .use(router)
    .mount('#app');
<template>
  <div id="app">
    <transition appear
                appear-active-class="animated zoomInUp"
                enter-active-class="animated zoomInUp"
                mode="out-in"
                name="custom-classes-transition">
      <router-view/>
    </transition>
  </div>
</template>
<script>
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'app',
  components: {
    HelloWorld
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>

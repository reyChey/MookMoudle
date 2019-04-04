<template>
  <div class="login">
    <img src="./../assets/logo.png" width="200" height="200">
    <h1>{{ title }}</h1>
    <el-row>
      <el-col :span="8" :offset="8">
        <el-input v-model="username" placeholder="请输入用户名" @focus="signFlag=false">
          <template slot="prepend">用户名</template>
        </el-input>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8" :offset="8">
        <el-input v-model="password" placeholder="请输入密码" show-password :type="password" @focus="signFlag=false">
          <template slot="prepend">密码</template>
        </el-input>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="4" :offset="10">
        <el-button type="primary" @click="submit">提交</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: 'Login',
    data () {
      return {
        username: '',
        password: '',
        title: '墨客茶馆管理系统',
        signFlag: false
      }
    },
    methods: {
      submit () {
        // todo 具体的提交逻辑，1.获取input框的数据 2，通过axios提交数据
        let username = this.username
        let password = this.password
        // 用axios发送post请求
        window.axios.post('/api/signIn', { username, password }).then(res => {
          alert(res.headers.authorization)
        }).catch(() => {
          alert(res.data)
        })
      }
    }
  }
</script>

<style scoped>
  .login {
    margin-top: 8rem;
  }
  h1 {
    font-weight: normal;
  }

</style>

<script>
export default {
  name:'for-signup'
}
</script>

<template>
  <div class="contain">
    <div class="box">
      <h2>Sign up</h2>
      <div class="buts">
          <div class="signup-but">email</div>
          <div class="signup-but">google</div>
      </div>
    </div>
  </div>
</template>

<style>

.contain {
  background-color: #5b5b65;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  margin: auto;
  text-align: center;
}
.box {
  width: 29%;
  height: 67%;
  background-color: #eee;
  border: 1px solid #ccc;
  display: flex;
  flex-direction: column;
  border-radius: 35px;
  text-align: center;
  align-items: center;
  margin: 20px;
}
h2{
  margin-top: 0;
}
.buts{

  align-items: center;
  justify-items: center;
  float: left;
}
.signup-but{
  width: 100%;
  background-color: #8c9e8f;
  border-radius: 12px;
  margin-top: 100px;
}
</style>
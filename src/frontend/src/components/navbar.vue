<script>


export default {
  props: {
    logged: false,
    userType: '',
    username: 'username'
  },
  name: 'navbar'
}
</script>
<template>
  <div class="main-navbar shadow-sm sticky-top">
    <div class="top-navbar">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-2 my-auto d-none d-sm-none d-md-block d-lg-block">
            <h5 class="brand-name">preeeeerd</h5>
          </div>
          <div class="col-md-5 my-auto">
            <div id="cover">
              <form method="get" action="">
                <div class="tb">
                  <div class="td"><input type="text" placeholder="Search" required></div>
                  <div class="td" id="s-cover">
                    <button type="submit">
                      <div id="s-circle"></div>
                      <span></span>
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
          <div class="col-md-5 my-auto">
            <ul class="nav justify-content-end">

              <li class="nav-item" v-if="!logged">
                <a class="nav-link" href="/signin">
                  Sign in
                </a>
              </li>
              <li class="nav-item" v-if="!logged">
                <a class="nav-link" href="/signup">
                  Sign up
                </a>
              </li>

              <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle"  id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                  <i class="fa fa-user"></i>{{ username }}
                </a>
                <ul class="dropdown-menu" aria-labelledby="navbarDropdown" v-if="logged">
                  <li><a class="dropdown-item"><i class="fa fa-user"></i> Profile</a></li>
                  <li v-if="userType !== 'client'"><a class="dropdown-item"><i class="fa fa-user"></i> My company</a></li>
                  <li v-if="userType !== 'client'"><a class="dropdown-item"><i class="fa fa-list"></i> My timetable</a></li>
                  <li><a class="dropdown-item" href="#"><i class="fa fa-heart"></i> My Wishlist</a></li>
                  <li><a class="dropdown-item" href="#"><i class="fa fa-sign-out"></i> Logout</a></li>
                </ul>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>


</template>



<style>

#cover{
  display: flex;
}

button {
  position: relative;
  display: block;
  width: 84px;
  height: 100%;
  cursor: pointer;
}
input[type="text"]::placeholder {
  color: #ffffff;
}

input[type="text"] {
  width: 100%;
  height: 80%;
  font-size: 40px;
  line-height: 1;
  text-align: left;
}

button span {
  position: absolute;
  top: 68px;
  left: 43px;
  display: block;
  width: 45px;
  height: 15px;
  background-color: transparent;
  border-radius: 10px;
  transform: rotateZ(52deg);
  transition: 0.5s ease all;
}

button span:before,
button span:after {
  content: "";
  bottom: 0;
  right: 0;
  width: 45px;
  height: 15px;
  background-color: #fff;
  border-radius: 10px;
  transition: 0.5s ease all;
  transform: rotateZ(52deg);
}

#s-circle {
  top: -8px;
  left: 10px;
  width: 43px;
  height: 43px;
  margin-top: 0;
  border: 15px solid #fff;
  background-color: transparent;
  border-radius: 50%;
}
#s-cover {
  width: 1px;
  padding-left: 20px;
}

input,
button {
  color: #fff;
  font-family: Nunito,serif;
  padding: 0;
  margin: 0;
  border: 0;
  background-color: transparent;
}

#cover {
  top: 50%;
  left: 0;
  right: 0;
  width: 100%;
  padding: 10px;
  margin: -83px auto 0 auto;
  background-color: #5b5b65;
  border-radius: 30px;
  transform: scale(0.6);
}
.main-navbar{
  border-bottom: 1px solid #fff8f8;
}
.brand-name{
  text-align: center;
  font-family: 'Courier New', Courier, monospace;
  color: #5c0606;
  font-size: 30px;
}
.main-navbar .top-navbar{
  background-color: gainsboro;
  padding-top: 5px;
  padding-bottom: 5px;
}

.main-navbar .top-navbar .nav-link{
  color: #5c0606;
  font-size: 20px;
  font-weight: 500;
}
.main-navbar .top-navbar .dropdown-menu{

}
.main-navbar .top-navbar .dropdown-menu .dropdown-item{
  padding: 8px 16px;
  border-bottom: 1px solid #f5ff4c;
  font-size: 14px;
}
.main-navbar .top-navbar .dropdown-menu .dropdown-item i{
  width: 20px;
  text-align: center;
  color: #711b1b;
  font-size: 14px;
}
.main-navbar .navbar{
  background-color: #a7a7c2;
}
.main-navbar .navbar .nav-item .nav-link{
  padding: 8px 20px;
  color: #000;
  font-size: 15px;
}

@media only screen and (max-width: 600px) {
  .main-navbar .top-navbar .nav-link{
    font-size: 12px;
    padding: 8px 10px;
  }
}

.tb {
  display: table;
  width: 100%;
}

.td {
  display: table-cell;
  vertical-align: middle;
}

</style>
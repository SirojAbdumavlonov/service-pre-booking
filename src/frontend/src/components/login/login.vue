<script setup>

export default {
  name:'login'
}

</script>

<template>
  <section class="vh-100" style="background-color: #eee;">
    <div class="container h-100">
      <div class="row d-flex justify-content-center align-items-center h-100">
        <div class="col-lg-12 col-xl-11">
          <div class="card text-black" style="border-radius: 25px;">
            <div class="card-body p-md-5">
              <div class="row justify-content-center">
                <div class="col-md-10 col-lg-6 col-xl-5 order-2 order-lg-1">
                  <p class="text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4">Logging in</p>

                  <form class="mx-1 mx-md-4" method="POST">
                    <div class="d-flex flex-row align-items-center mb-4">
                      <i class="fas fa-envelope fa-lg me-3 fa-fw"></i>
                      <div class="form-outline flex-fill mb-0">
                        <label class="form-label" for="form3Example3c">Your email or username</label>
                        <input type="email" id="form3Example3c" class="form-control" name = "email"/>
                        <span id="mistakeEmail" style="color: red" ></span>
                      </div>

                    </div>
                    <div class="d-flex flex-row align-items-center mb-4">
                      <i class="fas fa-lock fa-lg me-3 fa-fw"></i>
                      <div class="form-outline flex-fill mb-0">
                        <label class="form-label"  for="password">Password</label>
                        <input type="password" id="password" class="form-control" name="password" />
                      </div>
                    </div>
                    <div class="form-check">
                      <input class="form-check-input" type="radio" name="exampleRadios" id="exampleRadios1" value="admin" checked>
                      <label class="form-check-label" for="exampleRadios1">
                        employer
                      </label>
                    </div>
                    <div class="form-check">
                      <input class="form-check-input"  type="radio" name="exampleRadios" id="exampleRadios2" value="master">
                      <label class="form-check-label" for="exampleRadios2">
                        employee
                      </label>
                    </div>
                    <div class="form-check">
                      <input class="form-check-input"  type="radio" name="exampleRadios" id="exampleRadios3" value="client">
                      <label class="form-check-label" for="exampleRadios3">
                        client
                      </label>
                    </div>
                    <span id="radios"></span>

                    <a href="/signup" >Not registered yet? Let's register</a>
                    <div class="d-flex justify-content-center mx-4 mb-3 mb-lg-4">
                      <button type="submit" id="buttonReg" class="btn btn-primary btn-lg">Logging in</button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>

</style>
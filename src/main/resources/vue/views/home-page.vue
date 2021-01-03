<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} users</h5>
            <a href="/users" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Services</h5>
          <div class="card-body">
            <h5 class="card-title">{{services.length}} services</h5>
            <a href="/services" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>

    </div>

    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Available Packages</h5>
          <div class="card-body">
            <h5 class="card-title">{{packages.length}} packages</h5>
            <a href="/packages" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Promotions</h5>
          <div class="card-body">
            <h5 class="card-title">{{promotions.length}} Promotions</h5>
            <a href="/promotions" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>

    </div>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Available Packages</h5>
          <div class="card-body">
            <h5 class="card-title">{{employees.length}} packages</h5>
            <a href="/employees" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>



  </app-layout>
</template>
<script>
Vue.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        services: [],
        packages: [],
        promotions: [],
        employees:[],
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/services")
            .then(res => this.services = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/packages")
            .then(res => this.packages = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/promotions")
            .then(res => this.promotions = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/employees")
            .then(res => this.employees = res.data)
            .catch(() => alert("Error while fetching activities"));

      }
    });
</script>
<template id="employee-profile">
  <app-layout>
    <div v-if="noEmployeeFound">
      <p> We're sorry, we were not able to retrieve this employee.</p>
      <p> View <a :href="'/employees'">all employees</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noEmployeeFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Employee Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateEmployee()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteEmployee()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>

      <div class="card-body">
        <form>

          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-employee-id">Employee ID</span>
            </div>
            <input type="number" class="form-control" v-model="employee.id" name="id" readonly placeholder="Id"/>
          </div>


          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-employee-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="employee.e_name" name="e_name" placeholder="Name"/>
          </div>


          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-employee-phone">Phone</span>
            </div>
            <input type="number" class="form-control" v-model="employee.e_phone" name="e_phone" placeholder="Phone"/>
          </div>


          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-employee-designation">Designation</span>
            </div>
            <input type="text" class="form-control" v-model="employee.designation" name="designation" placeholder="designation"/>
          </div>

        </form>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("employee-profile", {
  template: "#employee-profile",
  data: () => ({
    employee: null,
    noEmployeeFound: false,
  }),
  created: function () {
    const employeeId = this.$javalin.pathParams["id"];
    const url = `/api/employees/${employeeId}`
    axios.get(url)
        .then(res => this.employee = res.data)
        .catch(error => {
          console.log("No employee found for id passed in the path parameter: " + error)
          this.noEmployeeFound = true
        });
  },
  methods: {
    updateEmployee: function () {
      const employeeId = this.$javalin.pathParams["id"];
      const url = `/api/employees/${employeeId}`
      axios.patch(url,
          {
            e_name: this.employee.e_name,
            e_phone: this.employee.e_phone,
            designation: this.employee.designation,

          })
          .then(response =>
              this.employee.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Employee updated!")
    },
    deleteEmployee: function () {
      if (confirm("Do you really want to delete?")) {
        const employeeId = this.$javalin.pathParams["id"];
        const url = `/api/employees/${employeeId}`
        axios.delete(url)
            .then(response => {
              alert("Employee deleted")
              //display the /employees endpoint
              window.location.href = '/employees';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});


</script>
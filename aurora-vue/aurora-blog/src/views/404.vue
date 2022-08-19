<template>
  <div id="not-found-page">
    <div class="background">
      <div class="ground"></div>
    </div>
    <div class="container">
      <div class="left-section">
        <div class="inner-content">
          <h1 class="heading">404</h1>
          <p class="subheading">
            Looks like the page you were looking for is no longer here.
          </p>
        </div>
      </div>
      <div class="right-section">
        <svg
          class="svgimg"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="51.5 -15.288 385 505.565"
        >
          <g class="bench-legs">
            <path
              d="M202.778,391.666h11.111v98.611h-11.111V391.666z M370.833,390.277h11.111v100h-11.111V390.277z M183.333,456.944h11.111
          v33.333h-11.111V456.944z M393.056,456.944h11.111v33.333h-11.111V456.944z"
            />
          </g>
          <g class="top-bench">
            <path
              d="M396.527,397.917c0,1.534-1.243,2.777-2.777,2.777H190.972c-1.534,0-2.778-1.243-2.778-2.777v-8.333
          c0-1.535,1.244-2.778,2.778-2.778H393.75c1.534,0,2.777,1.243,2.777,2.778V397.917z M400.694,414.583
          c0,1.534-1.243,2.778-2.777,2.778H188.194c-1.534,0-2.778-1.244-2.778-2.778v-8.333c0-1.534,1.244-2.777,2.778-2.777h209.723
          c1.534,0,2.777,1.243,2.777,2.777V414.583z M403.473,431.25c0,1.534-1.244,2.777-2.778,2.777H184.028
          c-1.534,0-2.778-1.243-2.778-2.777v-8.333c0-1.534,1.244-2.778,2.778-2.778h216.667c1.534,0,2.778,1.244,2.778,2.778V431.25z"
            />
          </g>
          <g class="bottom-bench">
            <path
              d="M417.361,459.027c0,0.769-1.244,1.39-2.778,1.39H170.139c-1.533,0-2.777-0.621-2.777-1.39v-4.86
          c0-0.769,1.244-0.694,2.777-0.694h244.444c1.534,0,2.778-0.074,2.778,0.694V459.027z"
            />
            <path
              d="M185.417,443.75H400c0,0,18.143,9.721,17.361,10.417l-250-0.696C167.303,451.65,185.417,443.75,185.417,443.75z"
            />
          </g>
          <g id="lamp">
            <path
              class="lamp-details"
              d="M125.694,421.997c0,1.257-0.73,3.697-1.633,3.697H113.44c-0.903,0-1.633-2.44-1.633-3.697V84.917
          c0-1.257,0.73-2.278,1.633-2.278h10.621c0.903,0,1.633,1.02,1.633,2.278V421.997z"
            />
            <path
              class="lamp-accent"
              d="M128.472,93.75c0,1.534-1.244,2.778-2.778,2.778h-13.889c-1.534,0-2.778-1.244-2.778-2.778V79.861
          c0-1.534,1.244-2.778,2.778-2.778h13.889c1.534,0,2.778,1.244,2.778,2.778V93.75z"
            />

            <circle class="lamp-light" cx="119.676" cy="44.22" r="40.51" />
            <path
              class="lamp-details"
              d="M149.306,71.528c0,3.242-13.37,13.889-29.861,13.889S89.583,75.232,89.583,71.528c0-4.166,13.369-13.889,29.861-13.889
          S149.306,67.362,149.306,71.528z"
            />
            <radialGradient
              class="light-gradient"
              id="SVGID_1_"
              cx="119.676"
              cy="44.22"
              r="65"
              gradientUnits="userSpaceOnUse"
            >
              <stop offset="0%" style="stop-color: #ffffff; stop-opacity: 1" />
              <stop offset="50%" style="stop-color: #ededed; stop-opacity: 0.5">
                <animate
                  attributeName="stop-opacity"
                  values="0.0; 0.5; 0.0"
                  dur="5000ms"
                  repeatCount="indefinite"
                ></animate>
              </stop>
              <stop
                offset="100%"
                style="stop-color: #ededed; stop-opacity: 0"
              />
            </radialGradient>
            <circle
              class="lamp-light__glow"
              fill="url(#SVGID_1_)"
              cx="119.676"
              cy="44.22"
              r="65"
            />
            <path
              class="lamp-bottom"
              d="M135.417,487.781c0,1.378-1.244,2.496-2.778,2.496H106.25c-1.534,0-2.778-1.118-2.778-2.496v-74.869
          c0-1.378,1.244-2.495,2.778-2.495h26.389c1.534,0,2.778,1.117,2.778,2.495V487.781z"
            />
          </g>
        </svg>
      </div>
    </div>
  </div>
</template>

<style lang="css" scoped>
@import url('https://fonts.googleapis.com/css?family=Fira+Sans');
/*Variables*/
.left-section .inner-content {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
}

#not-found-page {
  margin: 0;
  padding: 0;
  color: var(--text-noraml);
}

.background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    var(--background-primary),
    var(--background-secondary)
  );
  border-radius: 18px;
}
.background .ground {
  @apply shadow-xl;
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 25vh;
  background: #0c0e10;
  border-bottom-left-radius: 18px;
  border-bottom-right-radius: 18px;
}
@media (max-width: 770px) {
  .background .ground {
    height: 0vh;
  }
}

.container {
  position: relative;
  margin: 0 auto;
  width: 85%;
  height: 100vh;
  padding-bottom: 25vh;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  border-radius: 18px;
}
@media (max-width: 770px) {
  .container {
    flex-direction: column;
    padding-bottom: 0vh;
  }
}

.left-section,
.right-section {
  position: relative;
}

.left-section {
  width: 40%;
}
@media (max-width: 770px) {
  .left-section {
    width: 100%;
    height: 40%;
    position: absolute;
    top: 0;
  }
}
@media (max-width: 770px) {
  .left-section .inner-content {
    position: relative;
    padding: 1rem 0;
  }
}

.heading {
  text-align: center;
  font-size: 9em;
  line-height: 1.3em;
  margin: 2rem 0 0.5rem 0;
  padding: 0;
  text-shadow: 0 0 1rem #fefefe;
}
@media (max-width: 770px) {
  .heading {
    font-size: 7em;
    line-height: 1.15;
    margin: 0;
  }
}

.subheading {
  text-align: center;
  max-width: 480px;
  font-size: 1.5em;
  line-height: 1.15em;
  padding: 0 1rem;
  margin: 0 auto;
}
@media (max-width: 770px) {
  .subheading {
    font-size: 1.3em;
    line-height: 1.15;
    max-width: 100%;
  }
}

.right-section {
  width: 50%;
}
@media (max-width: 770px) {
  .right-section {
    width: 100%;
    height: 60%;
    position: absolute;
    bottom: 0;
  }
}

.svgimg {
  position: absolute;
  bottom: 0;
  padding-top: 10vh;
  padding-left: 1vh;
  max-width: 100%;
  max-height: 100%;
}
@media (max-width: 770px) {
  .svgimg {
    padding: 0;
  }
}
.svgimg .bench-legs {
  fill: #0c0e10;
}
.svgimg .top-bench,
.svgimg .bottom-bench {
  stroke: #0c0e10;
  stroke-width: 1px;
  fill: #5b3e2b;
}
.svgimg .bottom-bench path:nth-child(1) {
  fill: #432d20;
}
.svgimg .lamp-details {
  fill: #202425;
}
.svgimg .lamp-accent {
  fill: #2c3133;
}
.svgimg .lamp-bottom {
  fill: linear-gradient(#202425, #0c0e10);
}
.svgimg .lamp-light {
  fill: #efefef;
}

@keyframes glow {
  0% {
    text-shadow: 0 0 1rem #fefefe;
  }
  50% {
    text-shadow: 0 0 1.85rem #ededed;
  }
  100% {
    text-shadow: 0 0 1rem #fefefe;
  }
}
</style>

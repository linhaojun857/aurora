<template>
  <transition name="fade-bounce-y" mode="out-in">
    <div v-show="showDia" id="bot-container">
      <div id="Aurora-Dia--body" :style="cssVariables">
        <div id="Aurora-Dia--tips-wrapper">
          <div id="Aurora-Dia--tips" class="Aurora-Dia--tips">早上好呀～</div>
        </div>
        <div id="Aurora-Dia" class="Aurora-Dia">
          <div id="Aurora-Dia--eyes" class="Aurora-Dia--eyes">
            <div id="Aurora-Dia--left-eye" class="Aurora-Dia--eye left"></div>
            <div id="Aurora-Dia--right-eye" class="Aurora-Dia--eye right"></div>
          </div>
        </div>
        <div class="Aurora-Dia--platform"></div>
      </div>
    </div>
  </transition>
</template>

<script lang="ts">
// @ts-nocheck
import { computed, defineComponent, onMounted, ref } from 'vue'
import { useDiaStore } from '@/stores/dia'
import { useAppStore } from '@/stores/app'

export default defineComponent({
  name: 'Dia',
  setup() {
    const diaStore = useDiaStore()
    const appStore = useAppStore()
    const showDia = ref(false)
    onMounted(() => {
      initializeBot()
    })
    const initializeBot = () => {
      if (!appStore.aurora_bot_enable) return
      diaStore.initializeBot({
        locale: diaStore.aurora_bot.locale,
        tips: diaStore.aurora_bot.tips
      })
      setTimeout(() => {
        showDia.value = true
      }, 1000)
    }
    return {
      cssVariables: computed(() => {
        return `
          --aurora-dia--linear-gradient: ${appStore.themeConfig.header_gradient_css};
          --aurora-dia--linear-gradient-hover: linear-gradient(
            to bottom,
            ${appStore.themeConfig.gradient.color_2},
            ${appStore.themeConfig.gradient.color_3}
          );
          --aurora-dia--platform-light: ${appStore.themeConfig.gradient.color_3};
        `
      }),
      showDia
    }
  }
})
</script>

<style lang="scss" scoped>
#bot-container {
  position: fixed;
  left: 20px;
  bottom: 0;
  z-index: 1000;
  width: 70px;
  height: 60px;
}
#Aurora-Dia--body {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  width: 100%;
  height: 100%;
  --auora-dia--width: 65px; /* 110px */
  --auora-dia--height: 50px; /* 95px */
  --auora-dia--hover-height: 60px; /* 105px */
  --auora-dia--jump-1: 55px; /* 95px */
  --auora-dia--jump-2: 60px; /* 100px */
  --auora-dia--jump-3: 45px; /* 85px */
  --auora-dia--eye-top: 10px; /* 25px */
  --auora-dia--eye-height: 15px; /* 25px */
  --auora-dia--eye-width: 8px; /* 15px */
  --auora-dia--eye-top: 10px; /* 20px */
  --auora-dia--platform-size: var(--auora-dia--jump-2); /* 100px */
  --auora-dia--platform-size-shake-1: 75px; /* 115px */
  --auora-dia--platform-size-shake-2: 45px; /* 115px */
  --auora-dia--platform-top: -15px; /* 0 */
  --aurora-dia--linear-gradient: var(--main-gradient); /* linear-gradient(to bottom, #5fc, #1a8) */
  --aurora-dia--linear-gradient-hover: linear-gradient(to bottom, #25b0cc, #3f60de);
  --aurora-dia--platform-light: #b712ac;
}
.Aurora-Dia {
  position: absolute;
  bottom: 30px;
  width: var(--auora-dia--width);
  height: var(--auora-dia--height);
  border-radius: 45%;
  border: 4px solid var(--background-secondary);
  // box-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
  animation: breathe-and-jump 3s linear infinite;
  cursor: pointer;
  z-index: 1;
}
.Aurora-Dia::before {
  content: '';
  position: absolute;
  top: -1px;
  left: -1px;
  width: calc(100% + 3px);
  height: calc(100% + 2px);
  background-color: #2cdcff;
  background: var(--aurora-dia--linear-gradient);
  border-radius: 45%;
  opacity: 0;
  opacity: 1;
  transition: 0.3s linear all;
}
.Aurora-Dia.active {
  animation: deactivate 0.75s linear, bounce-then-breathe 5s linear infinite;
}

.Aurora-Dia--eyes > .Aurora-Dia--eye {
  position: absolute;
  top: var(--auora-dia--eye-top);
  width: var(--auora-dia--eye-width);
  height: var(--auora-dia--eye-height);
  border-radius: 15px;
  background-color: #fff;
  box-shadow: 0 0 7px rgba(255, 255, 255, 0.5);
  animation: blink 5s linear infinite;
}
.Aurora-Dia--eyes > .Aurora-Dia--eye.left {
  left: 25%;
}
.Aurora-Dia--eyes > .Aurora-Dia--eye.right {
  right: 25%;
}
.Aurora-Dia--eyes.moving > .Aurora-Dia--eye {
  animation: none;
}

.Aurora-Dia--platform {
  position: relative;
  top: 0;
  transform: rotateX(70deg);
  width: var(--auora-dia--platform-size);
  height: var(--auora-dia--platform-size);
  box-shadow: 0 0 var(--auora-dia--platform-size) var(--aurora-dia--platform-light),
    0 0 15px var(--aurora-dia--platform-light) inset;
  animation: jump-pulse 3s linear infinite;
}

.Aurora-Dia--platform {
  border-radius: 50%;
  transition: 0.2s linear all;
}

.Aurora-Dia:hover {
  animation: shake-to-alert 0.5s linear;
  height: var(--auora-dia--hover-height);
  transform: translateY(-7px);
}
.Aurora-Dia:hover::before {
  background: var(--aurora-dia--linear-gradient-hover);
}
.Aurora-Dia:hover,
.Aurora-Dia:hover > .Aurora-Dia--eyes > .Aurora--Dia-eye {
  border-color: var(--text-accent);
  box-shadow: 0 0 5px var(--text-accent);
}
.Aurora-Dia:hover + .Aurora-Dia--platform {
  box-shadow: 0 0 var(--auora-dia--platform-size) var(--text-accent), 0 0 15px var(--text-accent) inset;
  animation: shake-pulse 0.5s linear;
}

#Aurora-Dia--tips-wrapper {
  position: absolute;
  bottom: 80px;
  right: -120px;
  width: 200px;
  min-height: 60px;
  background: var(--aurora-dia--linear-gradient);
  color: var(--text-normal);
  padding: 0.2rem;
  border-radius: 8px;
  opacity: 0;
  animation: tips-breathe 3s linear infinite;
  transition: 0.3s linear opacity;
}

#Aurora-Dia--tips-wrapper.active {
  opacity: 0.86;
}

.Aurora-Dia--tips {
  position: relative;
  height: 100%;
  width: 100%;
  min-height: 60px;
  border-radius: 6px;
  padding: 0.2rem 0.5rem;
  font-size: 0.8rem;
  font-weight: 800;
  background: var(--background-secondary);
  overflow: hidden;
  text-overflow: ellipsis;
}

.Aurora-Dia--tips > span {
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  padding: 0 0.1rem;
  color: #7aa2f7;
  background-color: #7aa2f7;
  background-image: var(--strong-gradient);
}

@keyframes deactivate {
  0% {
    border-color: var(--text-sub-accent);
  }
  20%,
  60% {
    border-color: var(--text-accent);
  }
  40%,
  80%,
  100% {
    border-color: var(--background-secondary);
  }
}

@keyframes tips-breathe {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

@keyframes bounce-then-breathe {
  0%,
  5%,
  10%,
  15% {
    transform: translateY(0);
  }
  2.5%,
  7.5%,
  12.5% {
    transform: translateY(-15px);
  }
  20%,
  40%,
  60%,
  80%,
  100% {
    height: var(--auora-dia--jump-1);
    transform: translateY(0);
  }
  30%,
  50%,
  70%,
  90% {
    height: var(--auora-dia--jump-2);
    transform: translateY(-5px);
  }
}

@keyframes breathe-and-jump {
  0%,
  40%,
  80%,
  100% {
    height: var(--auora-dia--jump-1);
    transform: translateY(0);
  }
  20%,
  60%,
  70%,
  90% {
    height: var(--auora-dia--jump-2);
    transform: translateY(-5px);
  }
  85% {
    height: var(--auora-dia--jump-3);
    transform: translateY(-20px);
  }
}

@keyframes blink {
  0%,
  100% {
    transform: scale(1, 0.05);
  }
  5%,
  95% {
    transform: scale(1, 1);
  }
}

@keyframes jump-pulse {
  0%,
  40%,
  80%,
  100% {
    box-shadow: 0 0 30px var(--aurora-dia--platform-light), 0 0 45px var(--aurora-dia--platform-light) inset;
  }
  20%,
  60%,
  70%,
  90% {
    box-shadow: 0 0 70px var(--aurora-dia--platform-light), 0 0 25px var(--aurora-dia--platform-light) inset;
  }
  85% {
    box-shadow: 0 0 100px var(--aurora-dia--platform-light), 0 0 15px var(--aurora-dia--platform-light) inset;
  }
}

@keyframes shake-to-alert {
  0%,
  20%,
  40%,
  60%,
  80%,
  100% {
    transform: rotate(0) translateY(-8px);
  }
  10%,
  25%,
  35%,
  50%,
  65% {
    transform: rotate(7deg) translateY(-8px);
  }
  15%,
  30%,
  45%,
  55%,
  70% {
    transform: roate(-7deg) translateY(-8px);
  }
}

@keyframes shake-pulse {
  0%,
  20%,
  40%,
  60%,
  80%,
  100% {
    box-shadow: 0 0 var(--auora-dia--platform-size) #2cdcff, 0 0 15px #2cdcff inset;
  }
  10%,
  25%,
  35%,
  50%,
  65% {
    box-shadow: 0 0 var(--auora-dia--platform-size-shake-1) #2cdcff, 0 0 15px #2cdcff inset;
  }
  15%,
  30%,
  45%,
  55%,
  70% {
    box-shadow: 0 0 var(--auora-dia--platform-size-shake-2) #2cdcff, 0 0 15px #2cdcff inset;
  }
}
</style>

<style lang="scss">
.Aurora-Dia--tips > span {
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  padding: 0 0.05rem;
  color: #7aa2f7;
  background-color: #7aa2f7;
  background-image: var(--strong-gradient);
}
</style>

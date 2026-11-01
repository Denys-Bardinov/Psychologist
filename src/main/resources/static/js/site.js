// Small browser scripts for the page. No libraries.
// Each block looks for its element and does nothing if the page does not have it.
(function () {
  'use strict';

  var COOKIE_DAYS = 365;

  function setCookie(name, value) {
    document.cookie = name + '=' + encodeURIComponent(value) + '; path=/; max-age=' + COOKIE_DAYS * 86400 + '; SameSite=Lax';
  }

  function getCookie(name) {
    var match = document.cookie.match(new RegExp('(?:^|; )' + name + '=([^;]*)'));
    return match ? decodeURIComponent(match[1]) : null;
  }

  // ---------- Mobile menu (full screen) ----------
  var menu = document.getElementById('mmenu');
  var menuOpen = document.querySelector('[data-menu-open]');
  if (menu && menuOpen) {
    var menuClose = menu.querySelector('[data-menu-close]');
    var setMenu = function (open) {
      menu.hidden = !open;
      menuOpen.setAttribute('aria-expanded', String(open));
      document.body.classList.toggle('locked', open);
      (open ? menuClose : menuOpen).focus();
    };
    menuOpen.addEventListener('click', function () { setMenu(true); });
    menuClose.addEventListener('click', function () { setMenu(false); });
    menu.querySelectorAll('a').forEach(function (a) {
      a.addEventListener('click', function () { setMenu(false); });
    });
    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && !menu.hidden) setMenu(false);
    });
  }

  // ---------- Language switcher (remembers the choice in a cookie) ----------
  var langWrap = document.querySelector('[data-lang-switcher]');
  if (langWrap) {
    var langBtn = langWrap.querySelector('button');
    var langList = langWrap.querySelector('ul');
    var setLang = function (open) {
      langList.hidden = !open;
      langBtn.setAttribute('aria-expanded', String(open));
    };
    langBtn.addEventListener('click', function () { setLang(langList.hidden); });
    document.addEventListener('click', function (e) {
      if (!langWrap.contains(e.target)) setLang(false);
    });
    langWrap.addEventListener('keydown', function (e) {
      if (e.key === 'Escape') { setLang(false); langBtn.focus(); }
    });
    langList.querySelectorAll('a[data-lang]').forEach(function (a) {
      a.addEventListener('click', function () { setCookie('lang', a.dataset.lang); });
    });
  }

  // ---------- Dialogs: close buttons ----------
  document.querySelectorAll('dialog [data-dialog-close]').forEach(function (btn) {
    btn.addEventListener('click', function () { btn.closest('dialog').close(); });
  });
  document.querySelectorAll('dialog').forEach(function (dialog) {
    // Click on the dark backdrop closes the dialog.
    dialog.addEventListener('click', function (e) {
      if (e.target === dialog) dialog.close();
    });
  });

  // ---------- Floating chat button and bottom sheet ----------
  var chat = document.getElementById('chat');
  var chatOpen = document.querySelector('[data-chat-open]');
  if (chat && chatOpen) {
    chatOpen.addEventListener('click', function () { chat.showModal(); });
  }

  // ---------- Portfolio modal (arrows and Esc work from the keyboard) ----------
  var workModal = document.getElementById('work-modal');
  var workData = document.getElementById('work-data');
  if (workModal && workData) {
    var works = Array.prototype.map.call(workData.content.children, function (el) {
      return {
        title: el.querySelector('.title').textContent,
        category: el.querySelector('.category').textContent,
        text: el.querySelector('.text').textContent,
        image: el.querySelector('.image').textContent
      };
    });
    var current = 0;
    var showWork = function (index) {
      current = (index + works.length) % works.length;
      var w = works[current];
      workModal.querySelector('[data-work-title]').textContent = w.title;
      workModal.querySelector('[data-work-category]').textContent = w.category;
      workModal.querySelector('[data-work-text]').textContent = w.text;
      var img = workModal.querySelector('[data-work-image]');
      img.textContent = w.image;
      img.className = 'img bg-' + (current % 4);
    };
    document.querySelectorAll('[data-work]').forEach(function (btn) {
      btn.addEventListener('click', function () {
        showWork(Number(btn.dataset.work));
        workModal.showModal();
      });
    });
    workModal.querySelectorAll('[data-work-step]').forEach(function (btn) {
      btn.addEventListener('click', function () { showWork(current + Number(btn.dataset.workStep)); });
    });
    workModal.addEventListener('keydown', function (e) {
      if (e.key === 'ArrowLeft') showWork(current - 1);
      if (e.key === 'ArrowRight') showWork(current + 1);
    });
  }

  // ---------- Reviews slider (no autoplay, swipe on touch screens) ----------
  var slider = document.querySelector('[data-slider]');
  if (slider) {
    var section = slider.closest('section');
    var track = slider.querySelector('.track');
    var slides = slider.querySelectorAll('.slide');
    var dots = section.querySelectorAll('[data-slide-to]');
    var active = 0;
    var go = function (index) {
      active = (index + slides.length) % slides.length;
      track.style.transform = 'translateX(' + (-100 * active) + '%)';
      slides.forEach(function (s, i) {
        if (i === active) s.removeAttribute('aria-hidden'); else s.setAttribute('aria-hidden', 'true');
      });
      dots.forEach(function (d, i) {
        if (i === active) d.setAttribute('aria-current', 'true'); else d.removeAttribute('aria-current');
      });
    };
    section.querySelectorAll('[data-slide-step]').forEach(function (btn) {
      btn.addEventListener('click', function () { go(active + Number(btn.dataset.slideStep)); });
    });
    dots.forEach(function (d) {
      d.addEventListener('click', function () { go(Number(d.dataset.slideTo)); });
    });
    var startX = null;
    slider.addEventListener('touchstart', function (e) { startX = e.touches[0].clientX; }, { passive: true });
    slider.addEventListener('touchend', function (e) {
      if (startX === null) return;
      var dx = e.changedTouches[0].clientX - startX;
      if (Math.abs(dx) > 40) go(active + (dx < 0 ? 1 : -1));
      startX = null;
    });
  }

  // ---------- Cookie banner (first visit) ----------
  var banner = document.querySelector('[data-cookie]');
  if (banner) {
    var showBanner = function (show) {
      banner.hidden = !show;
      document.body.classList.toggle('has-cookie', show);
    };
    if (!getCookie('cookie_consent')) showBanner(true);
    banner.querySelectorAll('[data-cookie-choice]').forEach(function (btn) {
      btn.addEventListener('click', function () {
        setCookie('cookie_consent', btn.dataset.cookieChoice);
        showBanner(false);
      });
    });
    document.querySelectorAll('[data-cookie-settings]').forEach(function (btn) {
      btn.addEventListener('click', function () { showBanner(true); banner.querySelector('button').focus(); });
    });
  }

  // ---------- "Book" buttons on service cards pre-select the session type ----------
  document.querySelectorAll('[data-service]').forEach(function (btn) {
    btn.addEventListener('click', function () {
      var select = document.getElementById('f-type');
      if (select) select.selectedIndex = Number(btn.dataset.service);
    });
  });

  // ---------- Booking form ----------
  var form = document.querySelector('[data-booking]');
  if (form) {
    var card = form.parentElement;
    var nameInput = form.elements.namedItem('name');
    var contactInput = form.elements.namedItem('contact');
    var consentInput = form.elements.namedItem('consent');
    var contactLabel = form.querySelector('[data-contact-label]');
    var contactErr = document.getElementById('f-contact-err');
    var summary = form.querySelector('[data-error-summary]');
    var submit = form.querySelector('[data-submit]');
    var live = form.querySelector('[data-live]');
    var success = card.querySelector('[data-state="success"]');
    var fail = card.querySelector('[data-state="fail"]');
    var tried = false;

    var inputTypes = { phone: 'tel', email: 'email', instagram: 'text', messenger: 'text' };
    var autocomplete = { phone: 'tel', email: 'email', instagram: 'off', messenger: 'off' };

    var method = function () {
      return form.querySelector('input[name="method"]:checked').value;
    };

    var rules = {
      phone: function (v) { return v.replace(/\D/g, '').length >= 9; },
      email: function (v) { return /.+@.+\..+/.test(v); },
      instagram: function (v) { return v.length >= 3; },
      messenger: function (v) { return v.length >= 3; }
    };

    // The contact field label, hint, keyboard and error text follow the chosen method.
    form.querySelectorAll('input[name="method"]').forEach(function (radio) {
      radio.addEventListener('change', function () {
        contactLabel.textContent = radio.dataset.field;
        contactInput.placeholder = radio.dataset.ph;
        contactInput.type = inputTypes[radio.value];
        contactInput.autocomplete = autocomplete[radio.value];
        contactErr.textContent = radio.dataset.err;
        contactInput.value = '';
        if (tried) validate();
      });
    });

    var setError = function (input, errEl, bad) {
      input.setAttribute('aria-invalid', String(bad));
      errEl.hidden = !bad;
    };

    var validate = function () {
      var checks = [
        { input: nameInput, err: document.getElementById('f-name-err'), bad: !nameInput.value.trim() },
        { input: contactInput, err: contactErr, bad: !rules[method()](contactInput.value.trim()) },
        { input: consentInput, err: document.getElementById('f-consent-err'), bad: !consentInput.checked }
      ];
      checks.forEach(function (c) { setError(c.input, c.err, c.bad); });
      var bad = checks.filter(function (c) { return c.bad; });
      summary.hidden = bad.length === 0;
      form.querySelector('[data-error-list]').textContent = bad.map(function (c) {
        return c.err.textContent.trim();
      }).join('; ');
      return bad;
    };

    [nameInput, contactInput, consentInput].forEach(function (input) {
      input.addEventListener(input.type === 'checkbox' ? 'change' : 'input', function () {
        if (tried) validate();
      });
    });

    var setSending = function (sending) {
      form.setAttribute('aria-busy', String(sending));
      form.querySelectorAll('input, select, textarea, button').forEach(function (el) { el.disabled = sending; });
      submit.querySelector('[data-submit-label]').hidden = sending;
      submit.querySelector('[data-sending-label]').hidden = !sending;
      submit.querySelector('.spinner').hidden = !sending;
      if (sending) live.textContent = submit.querySelector('[data-sending-label]').textContent;
    };

    var showState = function (state) {
      form.hidden = state !== 'form';
      success.hidden = state !== 'success';
      fail.hidden = state !== 'fail';
      if (state === 'success') success.focus();
      if (state === 'fail') fail.focus();
    };

    form.addEventListener('submit', function (e) {
      e.preventDefault();
      tried = true;
      var bad = validate();
      if (bad.length) {
        bad[0].input.focus();
        live.textContent = summary.textContent.replace(/\s+/g, ' ').trim();
        return;
      }
      var data = {
        name: nameInput.value.trim(),
        method: method(),
        contact: contactInput.value.trim(),
        sessionType: form.elements.namedItem('sessionType').value,
        preferredTime: form.elements.namedItem('preferredTime').value.trim(),
        message: form.elements.namedItem('message').value.trim(),
        consent: consentInput.checked,
        lang: form.dataset.lang
      };
      setSending(true);
      fetch(form.action, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      }).then(function (res) {
        if (!res.ok) throw new Error('HTTP ' + res.status);
        setSending(false);
        showState('success');
      }).catch(function () {
        // Keep everything the visitor typed and offer a retry plus direct contacts.
        setSending(false);
        showState('fail');
      });
    });

    fail.querySelector('[data-retry]').addEventListener('click', function () {
      showState('form');
      form.requestSubmit();
    });

    success.querySelector('[data-again]').addEventListener('click', function () {
      form.reset();
      tried = false;
      summary.hidden = true;
      [nameInput, contactInput, consentInput].forEach(function (i) { i.removeAttribute('aria-invalid'); });
      form.querySelectorAll('.err').forEach(function (el) { el.hidden = true; });
      form.querySelector('input[name="method"]').dispatchEvent(new Event('change'));
      showState('form');
      nameInput.focus();
    });
  }
})();

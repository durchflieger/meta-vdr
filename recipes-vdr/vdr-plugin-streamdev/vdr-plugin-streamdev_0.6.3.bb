PR = "r1"
MAINTAINER = "df"
SECTION = "multimedia"

PN_CLIENT = "${PN}-client"
PN_SERVER = "${PN}-server"

PLUGIN_BASE_NAME = "${@d.getVar('PN').replace('vdr-plugin-', '')}"
PLUGIN_CLIENT_BASE_NAME = "${PLUGIN_BASE_NAME}-client"
PLUGIN_SERVER_BASE_NAME = "${PLUGIN_BASE_NAME}-server"

PLUGIN_CLIENT_CONF_FILE = "50-${PLUGIN_CLIENT_BASE_NAME}.conf"
PLUGIN_SERVER_CONF_FILE = "50-${PLUGIN_SERVER_BASE_NAME}.conf"

SUMMARY = "VDR Plugin to stream Live-TV to other VDR's"

DESCRIPTION = "This plugin for vdr lets the software stream videos into \
the network. You can interconnect several vdrs that way \
or watch those streams with special client apps like video \
lan client or mplayer."

HOMEPAGE = "https://github.com/vdr-projects/${PN}"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/vdr-projects/vdr-plugin-streamdev;branch=master;protocol=https \
  file://00-makefile.patch \
"

SRCREV="da74779591827ad7e10493b0eade65a11c525171"

S = "${WORKDIR}/git"

inherit gettext pkgconfig

DEPENDS = "vdr openssl"

PACKAGES = "${PN}-dbg ${PN_CLIENT} ${PN_SERVER} ${PN}-doc"

RDEPENDS:${PN_CLIENT} =+ "vdr"
RDEPENDS:${PN_SERVER} =+ "vdr bash"

FILES:${PN_CLIENT} =+ "${libdir}/vdr/plugins/*-client.so.*"
FILES:${PN_CLIENT} =+ "${datadir}/locale/*/*/*-client.mo"
FILES:${PN_CLIENT} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_CLIENT_CONF_FILE}"

FILES:${PN_SERVER} =+ "${libdir}/vdr/plugins/*-server.so.*"
FILES:${PN_SERVER} =+ "${datadir}/locale/*/*/*-server.mo"
FILES:${PN_SERVER} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_SERVER_CONF_FILE}"
FILES:${PN_SERVER} =+ "${sysconfdir}/vdr/plugins/streamdevhosts.conf"
FILES:${PN_SERVER} =+ "${localstatedir}/lib/vdr/plugins/${PLUGIN_SERVER_BASE_NAME}/streamdevhosts.conf"
FILES:${PN_SERVER} =+ "${datadir}/vdr/plugins/externremux.sh"
FILES:${PN_SERVER} =+ "${localstatedir}/lib/vdr/plugins/${PLUGIN_SERVER_BASE_NAME}/externremux.sh"

CONFFILES:${PN_CLIENT} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_CLIENT_CONF_FILE}"

CONFFILES:${PN_SERVER} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_SERVER_CONF_FILE}"
CONFFILES:${PN_SERVER} =+ "${sysconfdir}/vdr/plugins/streamdevhosts.conf"

do_install () {
  oe_runmake install DESTDIR=${D}

  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/README
  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/CONTRIBUTORS
  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/PROTOCOL

  echo "[${PLUGIN_CLIENT_BASE_NAME}]" > ${WORKDIR}/${PLUGIN_CLIENT_CONF_FILE}
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/${PLUGIN_CLIENT_CONF_FILE}

  echo "[${PLUGIN_SERVER_BASE_NAME}]" > ${WORKDIR}/${PLUGIN_SERVER_CONF_FILE}
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/${PLUGIN_SERVER_CONF_FILE}

  install -D -m 0644 -t ${D}${sysconfdir}/vdr/plugins ${S}/${PLUGIN_SERVER_BASE_NAME}/streamdevhosts.conf
  install -D -m 0755 -t ${D}${datadir}/vdr/plugins ${S}/${PLUGIN_SERVER_BASE_NAME}/externremux.sh

  install -d -m 0755 ${D}${localstatedir}/lib/vdr/plugins/${PLUGIN_SERVER_BASE_NAME}
  ln -s ${sysconfdir}/vdr/plugins/streamdevhosts.conf ${D}${localstatedir}/lib/vdr/plugins/${PLUGIN_SERVER_BASE_NAME}
  ln -s ${datadir}/vdr/plugins/externremux.sh ${D}${localstatedir}/lib/vdr/plugins/${PLUGIN_SERVER_BASE_NAME}
}
